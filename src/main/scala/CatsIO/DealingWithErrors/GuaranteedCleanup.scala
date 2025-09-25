
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.DealingWithErrors

import cats.effect.kernel.Outcome
import cats.effect.{ExitCode, IO, IOApp}

object GuaranteedCleanup extends IOApp:
  def log[T](message: String, instance: T): T =
    println(message + ": " + instance.toString)
    instance
  end log

  def GuaranteedCleanup_Program: IO[Unit] = for {
    fib <- IO(println("/Users/drmark/IdeaProjects/PLANE/src/main/scala/CatsIO/DealingWithErrors/GuaranteedCleanup.scala")).start
    l1 = log("time: 9/24/25:", fib.toString)
    _ <- fib.join
  } yield ()

  def readConfig(path: java.nio.file.Path): IO[String] =
    IO.blocking(java.nio.file.Files.readString(path))

  def withLoggedFinalizer(path: String): IO[String] =
    require(path != null)
    val nioPath = java.nio.file.Path.of(path)
    //guaranteeCase lets you observe whether the action completed successfully, errored, or was canceled, and still run a finalizer.
    readConfig(nioPath).guaranteeCase {
      case Outcome.Succeeded(_) => IO.println(s"[finally] read ok: $path")
      case Outcome.Errored(e) => IO.println(s"[finally] read failed: $path -> ${e.getMessage}")
      case Outcome.Canceled() => IO.println(s"[finally] read canceled: $path")
    }


  override def run(args: List[String]): IO[ExitCode] =
    //withLoggedFinalizer("/root/bad/path/to/file.txt").as(ExitCode.Success)
    withLoggedFinalizer("/Users/drmark/IdeaProjects/PLANE/src/main/scala/CatsIO/DealingWithErrors/GuaranteedCleanup.scala").as(ExitCode.Success)
