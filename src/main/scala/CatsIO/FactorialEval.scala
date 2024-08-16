package CatsIO

import CatsIO.Aid4Debugging.printStackContentEagerly
import cats.Eval
import cats.effect.{ExitCode, IO, IOApp}

object FactorialEval extends IOApp:
  def factorialEval(x: BigInt): Eval[BigInt] = if(x == 0) {
    Eval.now(1)
  } else {
    printStackContentEagerly()
    Eval.defer(factorialEval(x-1).map(_ * x))
  }

  def factorial(x:BigInt):BigInt = factorialEval(x).value
  override def run(args: List[String]): IO[ExitCode] =
    IO.println(factorial(100000)).as(ExitCode.Success)
