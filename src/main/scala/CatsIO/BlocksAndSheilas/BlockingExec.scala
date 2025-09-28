
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.BlocksAndSheilas

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits.*
import CatsIO.Helpers.Aid4Debugging.*
import CatsIO.Helpers.{bold, green}
import scala.util.{Failure, Success, Try}

object BlockingExec extends IOApp:
  def log[T](message: String, instance: T): T =
    println(message + ": " + instance.toString)
    instance
  end log

  def BlockingExec_Program: IO[Unit] = for {
    fib <- IO(println("/Users/drmark/IdeaProjects/PLANE/src/main/scala/CatsIO/BlocksAndSheilas/BlockingExec.scala")).start
    _ <- IO.blocking("on blocker inside BlockingExec_Program").debugInfo()
    l1 = log("time: 9/28/25:", fib.toString)
    _ <- fib.join
  } yield ()

  def withBlocker: IO[Unit] =
      for {
        _ <- IO.println("on default").debugInfo()
        res <- IO.blocking {
          println("on blocker inside withBlocker")
        }.debugInfo()
        _ <- IO.println("where am I?").debugInfo()
      } yield res

  override def run(args: List[String]): IO[ExitCode] =
      withBlocker.as(ExitCode.Success) *> BlockingExec_Program.as(ExitCode.Success)
