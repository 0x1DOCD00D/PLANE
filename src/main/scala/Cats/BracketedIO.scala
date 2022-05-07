package Cats

import cats.effect.{ExitCode, IO, IOApp}
import concurrent.duration.DurationInt
import scala.io.Source
import scala.util.{Failure, Success, Try}

object BracketedIO extends IOApp:
  var cntr:Int = 0
  def log[T] (message : String, instance : T): T =
    println(message + ": " + instance.toString)
    instance
  end log

  def manipulateFile(inputPath: String): IO[Unit] = IO {
    Try(Source.fromFile(inputPath)) match {
      case Success(file) => Option(file)//.toList.mkString
      case Failure(exception) =>
        println(exception.toString)
        None
    }
  }.bracket(fileHandler => IO(println(fileHandler.toString)) >> {
    val txt = fileHandler.toList.mkString
    IO(println("size = " + txt.length))
//    IO.unit
  })(fileHandler => IO{
    fileHandler.get.close()
    cntr = cntr + 1
    println(s"closing $cntr");})

  def bracketProgram: IO[Unit] = for {
    fib <- manipulateFile("/Users/drmark/IdeaProjects/PLANE/src/main/scala/Cats/BracketedIO.scala").start
    l1 = log("openfile: ", fib.toString)
    _ <- fib.join
  } yield ()


  override def run(args: List[String]): IO[ExitCode] = bracketProgram.as(ExitCode.Success)
