package Cats.ErrorHandling

import cats.MonadError
import cats.*
import cats.syntax.all.*

object MonadErrorExample {
  def retryWhile[F[_], E, A](
                              thunk: => F[A],
                              maxRetries: Int
                            )(p: E => Boolean)(using ME: MonadError[F, E]): F[A] =
    thunk.recoverWith { case e if p(e) && maxRetries > 0 =>
      retryWhile(thunk, maxRetries - 1)(p)
    }

  // Demo with Either
  sealed trait OpErr;

  case object Flaky extends OpErr;

  case object Fatal extends OpErr

  type Op[A] = Either[OpErr, A]
  var tries = 0

  def flakyOp: Op[Int] = {
    tries += 1; if (tries < 3) Left(Flaky) else Right(42)
  }

  val got: Op[Int] =
    retryWhile[Op, OpErr, Int](flakyOp, maxRetries = 5)(e => e == Flaky)
  // Right(42)

  val wontRetry: Op[Int] =
    retryWhile[Op, OpErr, Int](Left(Fatal), 5)(_ == Flaky)
  // Left(Fatal)

  def main(args: Array[String]): Unit = {
      println(s"Got: $got")
      println(s"Wont retry: $wontRetry")
  }
}
