package Cats

import cats.effect.kernel.Outcome.{Canceled, Errored, Succeeded}
import cats.effect.{ExitCode, IO, IOApp, Resource, Sync}
import cats.implicits.catsSyntaxApplicativeId
import cats.syntax.all.catsSyntaxApplicativeId

import scala.io.{BufferedSource, Source}
import scala.util.{Failure, Success, Try}
import Aid4Debugging.*

object ResourcesExperiments extends IOApp :
  trait ReturnResult4Resources
  case class ErrorMsg(msg:String) extends ReturnResult4Resources
  case class DataSizeFromFileContent(data:Int) extends ReturnResult4Resources
  case class ContentFromFile(data:String) extends ReturnResult4Resources
  final case class FileRuntimeError(e: Throwable) extends ReturnResult4Resources

  def processFileContent(content: Either[FileRuntimeError, ContentFromFile]): IO[Int] = content match
    case Left(e) =>
      println(s"Error: ${e.toString}")
      -1.pure[IO]

    case Right(v) =>
      val size = v match {
        case ContentFromFile(data) => data.length
      }
      for {
        dataSize <- Sync[IO].delay(size)
        _ = log("retrieved data size", dataSize)
      } yield dataSize

  def manipulateFile(inputPath: String): Resource[IO, BufferedSource] = Resource.make {
    IO.blocking {
      println(s"opening $inputPath")
      Source.fromFile(inputPath)
    }
  } {
  file =>
    IO.blocking {
      println(s"closing file: ${file.toString}")
      file.close()
    }
  }

  def getResult(i:Int):ReturnResult4Resources = if i >= 0 then DataSizeFromFileContent(i) else ErrorMsg("oops!")
  def obtainContentLength(inputPath: String): IO[ReturnResult4Resources] =
    import cats.implicits.catsSyntaxEither
    import cats.syntax.all.catsSyntaxEither
    import cats.syntax.either.catsSyntaxEither
    for {
      result <- manipulateFile(inputPath).use {
        file => IO.blocking {
          ContentFromFile(file.toList.mkString)
        }
      }.attempt
       .map(_.leftMap(Cats.ResourcesExperiments.FileRuntimeError.apply))
      ioLen <- processFileContent(result)
      output = getResult(ioLen)
    } yield output

  override def run(args: List[String]): IO[ExitCode] =
    val program = for {
      doIt <- obtainContentLength("/Users/drmark/IdeaProjects/PLANE/src/main/scala/Cats/ResourcesExperiments1.scala").start.showThreadAndData
      result <- doIt.join
    } yield result

    program.flatMap {
      case Succeeded(fa) =>
        for {
          ioLen <- fa
          _ = log("data len", ioLen)
        } yield ioLen
      case Errored(e) => IO.raiseError(e)
      case Canceled() => IO.raiseError(new RuntimeException("Computation canceled."))
    }.showThreadAndData >> IO.unit.as(ExitCode.Success)