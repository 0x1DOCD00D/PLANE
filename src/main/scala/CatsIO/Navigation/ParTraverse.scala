
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.Navigation

import CatsIO.Helpers.Aid4Debugging.debugInfo
import CatsIO.Helpers.{bold, green}
import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits.*

/*
from https://essentialeffects.dev/:

F[A] => (A => G[B]) => G[F[B]]
For example, if F is List and G is IO, then (par)traverse would be a function from a
List[A] to an IO[List[B]] when given a function A ⇒ IO[B].
List[A] => (A => IO[B]) => IO[List[B]]
*/

object ParTraverse extends IOApp:
  def log[T](message: String, instance: T): T =
    println(message + ": " + instance.toString)
    instance
  end log

  val tasks: List[Int] = (1 to 100).toList
  def task(id: Int): IO[Int] = IO(id).debugInfo()

  def ParTraverse_Program: IO[Unit] =
    tasks.parTraverse(task).debugInfo().map(r => log("Result: ".green.bold, r)).void

  override def run(args: List[String]): IO[ExitCode] = ParTraverse_Program.as(ExitCode.Success)
