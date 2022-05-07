package Cats

import cats.effect.kernel.Outcome.{Canceled, Errored, Succeeded}
import cats.effect.{ExitCode, IO, IOApp, Resource, Sync}

import scala.io.{BufferedSource, Source}
import scala.util.{Failure, Success, Try}
import Aid4Debugging.*

object ResourcesExperiments extends IOApp :

  def processFileContent(content: String): IO[Int] =
    for {
      dataSize <- Sync[IO].delay(content.length)
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
    }.handleErrorWith(err => {
      println(err.toString)
      IO.unit
    })
  }

  def obtainContentLength(inputPath: String): IO[Int] =
    for {
      result <- manipulateFile(inputPath).use {
        file => IO.blocking {
          file.toList.mkString
        }
      }
      ioLen <- processFileContent(result)
    } yield ioLen

  override def run(args: List[String]): IO[ExitCode] =
    val program = for {
      doIt <- obtainContentLength("/Users/drmark/IdeaProjects/PLANE/src/main/scala/Cats/ResourcesExperiments.scala").start.showThreadAndData
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