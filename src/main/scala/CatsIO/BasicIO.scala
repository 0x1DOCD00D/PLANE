package CatsIO

import cats.effect._
import cats.effect.unsafe.implicits.global
import Aid4Debugging.*

object BasicIO extends IOApp:
  override def run(args: List[String]): IO[ExitCode] =
    val ioeff= IO { println("print me!") }

    val program =
      for {
        fib1 <- ioeff.start.showThreadAndData
        fib2 <- ioeff.start.showThreadAndData
        _ <- fib1.join
        _ <- fib2.join
      } yield ()

    program.as(ExitCode.Success)



