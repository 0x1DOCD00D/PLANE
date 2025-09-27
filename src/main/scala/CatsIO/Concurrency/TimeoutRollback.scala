
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.Concurrency

import cats.effect.{ExitCode, IO, IOApp}
import scala.util.{Failure, Success, Try}
import scala.concurrent.duration.*
import cats.effect.Outcome
import cats.syntax.all.*
import CatsIO.Helpers.Aid4Debugging.*

object TimeoutRollback extends IOApp:
  def log[T](message: String, instance: T): T =
    println(message + ": " + instance.toString)
    instance
  end log

  def TimeoutRollback_Program: IO[Unit] = for {
    fib <- IO(println("/Users/drmark/IdeaProjects/PLANE/src/main/scala/CatsIO/Concurrency/TimeoutRollback.scala")).start
    l1 = log("time: 9/27/25:", fib.toString)
    _ <- fib.join
  } yield ()

  def transactional[A](name: String)(op: IO[A]): IO[A] =
    IO.println(s"BEGIN $name") *>
      op.guaranteeCase {
        case Outcome.Succeeded(_) => IO.println(s"COMMIT $name")
        case Outcome.Errored(e) => IO.println(s"ROLLBACK $name due to " + e.getMessage)
        case Outcome.Canceled() => IO.println(s"ROLLBACK $name due to timeout")
      }

  // pretend this is a slow remote call
  val placeOrder: IO[String] =
    IO.println("reserving inventory...") *>
      IO.sleep(5.seconds) *>
        IO.pure("order-123")

  def program: IO[Unit] =
    for
      fib <- transactional("order")(placeOrder).start.debugInfo()
      _ <- IO.sleep(1.second) *> IO.println("SLA breached, cancel!").debugInfo()
      _ <- fib.cancel // must cancel
      out <- fib.join // observe cleanup path
      _ <- IO.println(s"child outcome = $out")
    yield ()

  override def run(args: List[String]): IO[ExitCode] =
    TimeoutRollback_Program *> program.as(ExitCode.Success)

