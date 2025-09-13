////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats

//a log example from https://github.com/rockthejvm/typelevel-rite-of-passage

object LoggerWithCats {
  import cats.*
  import cats.implicits.*
  import org.typelevel.log4cats.Logger

  /*
   * extension [F[_], E, A](fa: F[A]) … introduces two new methods, log and logError, for any value fa of type F[A]. Three type parameters appear:
    F[_] — the effect type (for example cats.effect.IO, EitherT[IO, ErrorCode, *], Task, etc.).
    E — the error channel carried by MonadError[F, E].
    A — the success value produced when the computation works.

    Two givens must be in scope for the extension to compile:
    MonadError[F, E] called me. It provides attempt, pure, and other combinators that expose or handle errors inside F.
    Logger[F] from log4cats. It can write messages at levels such as info, error, etc. inside the same effect F.
   * */

  extension [F[_], E, A](fa: F[A])(using me: MonadError[F, E], logger: Logger[F]) {
    def log(success: A => String, error: E => String): F[A] = 
      fa.attemptTap {
      /*
       * fa.attemptTap first converts fa to F[Either[E, A]] (attempt),
       * then calls the supplied function for its side effect (tap), and finally returns the original fa unchanged.
       * */
      case Left(e)  => logger.error(error(e))
      case Right(a) => logger.info(success(a))
    }

    def logError(error: E => String): F[A] = fa.attemptTap {
      case Left(e)  => logger.error(error(e))
      case Right(_) => ().pure[F]
    }
  }

  def main(args: Array[String]): Unit = {
    import cats.effect.*
    import org.typelevel.log4cats.slf4j.Slf4jLogger

    given Logger[IO] = Slf4jLogger.getLogger[IO]

    val program1: IO[Int] = IO(42).log(a => s"program1 success: $a", e => s"program1 error: $e")
    val program2: IO[Int] = IO.raiseError(new RuntimeException("BOOM")).log(a => s"program2 success: $a", e => s"program2 error: $e")

    val program3: IO[Int] = IO(100).logError(e => s"program3 error: $e")
    val program4: IO[Int] = IO.raiseError(new RuntimeException("CRASH")).logError(e => s"program4 error: $e")

    val combined = for {
      _ <- program1.handleError(_ => -1)
      _ <- program2.handleError(_ => -1)
      _ <- program3.handleError(_ => -1)
      _ <- program4.handleError(_ => -1)
    } yield ()

    println(combined) 
  }
}
