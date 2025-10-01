
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.AsyncComputations

import cats.effect.{ExitCode, IO, IOApp}

import java.util.concurrent.{ExecutorService, Executors}
import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.{Duration, DurationInt}
import CatsIO.Helpers.Aid4Debugging.*
import CatsIO.Helpers.ColorizeOutput.*
import CatsIO.Helpers.{bold, green}

trait SlummingWithAsyncTrait:
  val threadPool: ExecutorService = Executors.newFixedThreadPool(8)
  given ExecutionContext = ExecutionContext.fromExecutorService(threadPool)
  type Callback[A] = Either[Throwable, A] => Unit
end SlummingWithAsyncTrait

object SlummingWithAsync extends IOApp with SlummingWithAsyncTrait:
  def log[T](message: String, instance: T): T =
    println(message + ": " + instance.toString)
    instance
  end log

  def somePayloadComputation(td: Duration): String =
    Thread.sleep(1.second.toMillis)
    println(s"[${Thread.currentThread().getName}] computing the payload somewhere else".green.bold)
    "somePayloadComputation is done"

  def asyncComputation[A](td: Duration)(cb: Callback[A]): Unit =
      threadPool.execute(() =>
          try
            Thread.sleep(td.toMillis)
            val result: Try[A] = Success(somePayloadComputation(td).asInstanceOf[A])
            cb(result.toEither)
          catch
            case e: Throwable =>
                val result: Try[A] = Failure(e)
                cb(result.toEither)
      )

  def SlummingWithAsync_Program: IO[Unit] = for {
    fib <- IO(println("/Users/drmark/IdeaProjects/PLANE/src/main/scala/CatsIO/AsyncComputations/SlummingWithAsync.scala")).start
    l1 = log("time: 9/29/25:", fib.toString)
    _ <- fib.join
    _ <- IO.println("=== effect below ===".green.bold)
    res <- IO.async[String] { (cb: Callback[String]) =>
      IO {
        asyncComputation(2.seconds)(cb)
        None
      }
    }.debugInfo()

    _   <- IO.println(s"result: $res".bold)  } yield ()

  override def run(args: List[String]): IO[ExitCode] =
    SlummingWithAsync_Program.as(ExitCode.Success)