
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.DealingWithErrors

import cats.effect.{ExitCode, IO, IOApp}
import scala.util.{Failure, Success, Try}

object RetryOnFailures extends IOApp:
  def log[T](message: String, instance: T): T =
    println(message + ": " + instance.toString)
    instance
  end log

  def RetryOnFailures_Program: IO[Unit] = for {
    fib <- IO(println("/Users/drmark/IdeaProjects/PLANE/src/main/scala/CatsIO/DealingWithErrors/RetryOnFailures.scala")).start
    l1 = log("time: 9/24/25:", fib.toString)
    _ <- fib.join
  } yield ()


  def flakyService(i: Int): IO[String] =
    IO.delay {
      if i % 2 == 0 then throw new java.io.IOException("network hiccup")
      else if i % 3 == 0 then throw new IllegalArgumentException("bad input")
      else s"ok-$i"
    }

  def retryIO[A](ioa: IO[A], max: Int): IO[A] = {
    def loop(n: Int): IO[A] =
      ioa.handleErrorWith {
        case e: java.io.IOException if n < max =>
          IO.println(s"retrying after IO error: ${e.getMessage}; attempt ${n + 1}/$max") *> loop(n + 1)
        case e => IO.raiseError(e)
      }

    loop(0)
  }

  val fetched: IO[String] = retryIO(flakyService(2), max = 3)

  override def run(args: List[String]): IO[ExitCode] =
    IO.println("starting") *>
      fetched.flatMap(o => IO.println(s"fetched: $o")).handleErrorWith(e => IO.println(s"failed: ${e.getMessage}"))
        *> IO.println("done").as(ExitCode.Success)
