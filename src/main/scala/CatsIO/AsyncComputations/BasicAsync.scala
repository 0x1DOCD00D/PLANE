////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.AsyncComputations

import cats.effect.{ExitCode, IO, IOApp}

import java.util.concurrent.CompletableFuture
import java.util.function.BiFunction
import scala.util.{Failure, Success, Try}
import cats.effect.*
import cats.implicits.*
import CatsIO.Helpers.Aid4Debugging.*
import CatsIO.Helpers.{blue, bold, red}

object BasicAsync extends IOApp:
  def log[T](message: String, instance: T): T =
    println(message + ": " + instance.toString)
    instance
  end log

  def BasicAsync_Program: IO[Unit] = for {
    fib <- IO(println("/Users/drmark/IdeaProjects/PLANE/src/main/scala/CatsIO/AsyncComputations/BasicAsync.scala")).start.debugInfo()
    l1 = log("time: 9/29/25:", fib.toString)
    _ <- fib.join
    _ <- IO.println("=== effect below ===".red.bold)
    _ <- effect.debugInfo()
  } yield ()

  val effect: IO[String] = fromCF(IO(cf()))

  def fromCF[A](cfa: IO[CompletableFuture[A]]): IO[A] =
    cfa.flatMap { fa =>
      IO.async { cb =>
        def handlerBiFunction[B](cb: Either[Throwable, B] => Unit): BiFunction[B, Throwable, Unit] =
          (a: B, e: Throwable) => {
            val result: Try[B] = if (e != null) Failure(e) else Success(a)
            cb(result.toEither)
            ()
          }
        fa.handle(handlerBiFunction(cb))
        IO.pure(None).debugInfo()
      }
    }

  def cf(): CompletableFuture[String] =
    CompletableFuture.supplyAsync(() => "callback result".blue.bold)

  override def run(args: List[String]): IO[ExitCode] =
    BasicAsync_Program.as(ExitCode.Success) *> effect.debugInfo().as(ExitCode.Success)

