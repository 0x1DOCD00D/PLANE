package Cats

import cats.effect.{ExitCode, IO, IOApp}
import Aid4Debugging.*

object FactorialIO extends IOApp:

  def fact(n: Int): Int =
    Thread.currentThread().getStackTrace.foreach(println)
    if n <= 0 then 1 else n * fact(n - 1)

  def factIO(n: Int): IO[Int] = if n <= 0 then IO.delay(1) else
    for {
      N <- IO.delay(n)
      `N-1` <- factIO(n - 1)
      _ <- printStackContent
    } yield `N-1` * N

  override def run(args: List[String]): IO[ExitCode] =
    fact(5)
    val program = for {
      fib <- factIO(5).start.showThreadAndData
      result <- fib.join
      output <- putStrLn(result)
      _ = log("fact", result)
    } yield output
    program.as(ExitCode.Success)
