package Cats

import cats.MonadError
import cats.instances.either.*
import cats.effect.{ExitCode, IO, IOApp}
import cats.syntax.all.*

object CleaningTheMessFromErrors extends IOApp:
  def CheckTheGuess[F[_]](guess: Int): MonadError[F, Throwable] ?=> F[Int] =
    if(guess >= 100) guess.pure[F]
    else new Exception("Guess must be greater than or equal to 100").raiseError[F, Int]

  override def run(args: List[String]): IO[ExitCode] =
    val program = for {
      result <- CheckTheGuess(3)
    } yield result

    program match
      case Left(msg) => IO.println(msg).as(ExitCode.Error)
      case Right(guess) => IO.println(s"Correct number $guess").as(ExitCode.Success)
