
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.Concurrency

import cats.effect.{ExitCode, IO, IOApp}
import CatsIO.Helpers.Aid4Debugging.*
import cats.effect.syntax.all.*
import cats.effect.implicits.*
import cats.effect.*
import cats.implicits.*

import scala.util.{Failure, Success, Try}

object TimingIsEverything extends IOApp:
  def log[T](message: String, instance: T): T =
    println(message + ": " + instance.toString)
    instance
  end log

  def TimingIsEverything_Program: IO[ExitCode] = for {
    fib <- IO(println("/Users/drmark/IdeaProjects/PLANE/src/main/scala/CatsIO/Concurrency/TimingIsEverything.scala")).start
    l1 = log("time: 9/27/25:", fib.toString)
    _ <- fib.join
  } yield ExitCode.Success

  import scala.concurrent.duration._
  val task: IO[Unit] = annotatedSleep(" task", 1.second)
  val timeout: IO[Unit] = annotatedSleep("timeout", 500.millis)

  def annotatedSleep(name: String, duration: FiniteDuration): IO[Unit] = {
      IO(s"$name: starting").debugInfo() *> IO.sleep(duration).debugInfo() *> IO(s"$name: done").debugInfo()
  }.onCancel(IO(s"$name: cancelled").void).void.debugInfo()

  override def run(args: List[String]): IO[ExitCode] =
    TimingIsEverything_Program <* {
    for {
      done <- IO.race(task, timeout).debugInfo()
      _ <- done match {
        case Left(_) => IO.println(" task: won").debugInfo()
        case Right(_) => IO.println("timeout: won").debugInfo()
      }
    } yield ()}.as(ExitCode.Success)
