
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.Concurrency

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits.catsSyntaxParallelTraverse1
import CatsIO.Helpers.Aid4Debugging.debugInfo

object ExploreParallelism extends IOApp:
  def log[T](message: String, instance: T): T =
    println(message + ": " + instance.toString)
    instance
  end log

  def ExploreParallelism_Program: IO[Unit] = for {
    _ <- IO(s"number of CPUs: $numCpus").debugInfo()
    _ <- tasks.debugInfo()
    fib <- IO(println("/Users/drmark/IdeaProjects/PLANE/src/main/scala/CatsIO/Concurrency/ExploreParallelism.scala")).start
    l1 = log("time: 9/27/25:", fib.toString)
    _ <- fib.join
  } yield ()

  val numCpus: Int = Runtime.getRuntime.availableProcessors()
  val tasks: IO[List[Int]] = List.range(0, numCpus * 2).parTraverse(task)
  def task(i: Int): IO[Int] = IO(i).debugInfo(  )

  override def run(args: List[String]): IO[ExitCode] = ExploreParallelism_Program.as(ExitCode.Success)
