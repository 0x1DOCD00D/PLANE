
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.ExternalFrameworks

import cats.effect.{ExitCode, IO, IOApp}

import scala.util.{Failure, Success, Try}
import cats.*
import cats.syntax.all.*
import cats.implicits.*
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.{Logger, SelfAwareLogger}
import cats.*
import cats.syntax.all.*
import org.typelevel.log4cats.Logger

import java.nio.file.{Files, Paths}

extension [F[_], A](fa: F[A]) {

  /** Logs t with `Logger.error(t)(msg)` when it fails, rethrows afterward. */
  def logErrorT(msg: Throwable => String)
               (using F: MonadThrow[F], L: Logger[F]): F[A] =
    fa.attempt.flatTap {
      case Left(t)  => L.error(t)(msg(t))
      case Right(_) => F.unit
    }.rethrow

  /** Logs both branches, using Throwable-aware error logging. */
  def logBothT(onSuccess: A => String, onError: Throwable => String)
              (using F: MonadThrow[F], L: Logger[F]): F[A] =
    fa.attempt.flatTap {
      case Right(a) => L.info(onSuccess(a))
      case Left(t)  => L.error(t)(onError(t))
    }.rethrow
}

object CatsCoreLogging extends IOApp:
  given Logger[IO] = Slf4jLogger.getLogger[IO]

  def readFile(path: String): IO[String] =
    IO.blocking(Files.readString(Paths.get(path)))

  def risky: IO[String] = IO.delay(sys.env("MISSING_ENV"))

  def CatsCoreLogging_Program: IO[Unit] = for {
    fib <- IO(println("/Users/drmark/IdeaProjects/PLANE/src/main/scala/CatsIO/ExternalFrameworks/CatsCoreLogging.scala")).start
    _ <- List("41", "7", "42").parTraverse { s =>
        IO.delay(s.toInt)
          .logErrorT(t => s"failed to parse '$s': ${t.getMessage}")
          .map(_.some)
      }.flatMap { xs => IO.println(s"parsed: ${xs.flatten}")
    }
    _ <- readFile("target/logs/plane.log")
      .logBothT(
        s => s"read ${s.length} bytes from plane.log",
        t => s"read failed: ${t.getClass.getSimpleName}: ${t.getMessage}"
      )
      .void
    _ <- risky
      .logErrorT(t => s"risky failed: ${t.getClass.getSimpleName}: ${t.getMessage}")
      .void
    _ <- fib.join
  } yield ()

  override def run(args: List[String]): IO[ExitCode] = CatsCoreLogging_Program.as(ExitCode.Success)
