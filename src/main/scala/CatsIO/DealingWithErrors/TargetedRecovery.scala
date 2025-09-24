
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.DealingWithErrors

import cats.effect.{ExitCode, IO, IOApp}
import scala.util.{Failure, Success, Try}

object TargetedRecovery extends IOApp:
  def log[T](message: String, instance: T): T =
    println(message + ": " + instance.toString)
    instance
  end log

  def TargetedRecovery_Program: IO[Unit] = for {
    fib <- IO(println("/Users/drmark/IdeaProjects/PLANE/src/main/scala/CatsIO/DealingWithErrors/TargetedRecovery.scala")).start
    l1 = log("time: 9/24/25:", fib.toString)
    _ <- fib.join
  } yield ()

  def flakyService(i: Int): IO[String] =
    IO.delay {
      if i % 2 == 0 then throw new java.io.IOException("network hiccup")
      else if i % 3 == 0 then throw new IllegalArgumentException("bad input")
      else s"ok-$i"
    }

  def robust(i: Int): IO[String] =
    flakyService(i).handleErrorWith {
      case e: java.io.IOException =>
        IO.println(s"recovering from IO error: ${e.getMessage}") *> IO.pure("cached-value")
      case other =>
        IO.raiseError(other) // let unexpected errors fail the fiber
    }


  override def run(args: List[String]): IO[ExitCode] = robust(6).flatMap(o=>IO.println(o)).as(ExitCode.Success)
