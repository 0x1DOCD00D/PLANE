////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.DealingWithErrors

import cats.effect.{ExitCode, IO, IOApp}
import scala.util.{Failure, Success, Try}

object ResourceAcquireUseRelease extends IOApp:
  def log[T](message: String, instance: T): T =
    println(message + ": " + instance.toString)
    instance
  end log

  def ResourceAcquireUseRelease_Program: IO[Unit] = for {
    fib <- IO(println("/Users/drmark/IdeaProjects/PLANE/src/main/scala/CatsIO/DealingWithErrors/ResourceAcquireUseRelease.scala")).start
    l1 = log("time: 9/24/25:", fib.toString)
    _ <- fib.join
  } yield ()

  import cats.effect.kernel.Resource

  def open(path: java.nio.file.Path): IO[scala.io.BufferedSource] =
    IO.blocking(scala.io.Source.fromFile(path.toFile)).adaptError { case t =>
      new RuntimeException(s"open failed: $path", t)
    }

  def close(src: scala.io.BufferedSource): IO[Unit] =
    IO.blocking(src.close()).handleErrorWith { t =>
      IO.println(s"close failed (ignored): ${t.getMessage}") // never rethrow from release
    }

  def lineResource(path: String): Resource[IO, scala.io.BufferedSource] =
    require(path != null)
    val nioPath = java.nio.file.Path.of(path)
    Resource.make(open(nioPath))(close)

  def firstLine(path: String): IO[String] =
    lineResource(path).use { src =>
      IO.blocking(src.getLines().next()).adaptError { case t =>
        new RuntimeException(s"read failed: $path", t)
      }
    }

  override def run(args: List[String]): IO[ExitCode] =
    lineResource("/Users/drmark/IdeaProjects/PLANE/src/main/scala/CatsIO/DealingWithErrors/ResourceAcquireUseRelease.scala").use { src =>
      IO.blocking(src.getLines().next())
    }.attempt.flatMap {
      case Right(line) => IO.println(s"first line: $line")
      case Left(e)     => IO.println(s"[err] ${e.getMessage}")
    }.as(ExitCode.Success)