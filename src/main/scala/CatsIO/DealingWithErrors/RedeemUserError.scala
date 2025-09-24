
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.DealingWithErrors

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits.catsSyntaxApplicativeByName

import scala.util.{Failure, Success, Try}

object RedeemUserError extends IOApp:
  def log[T](message: String, instance: T): T =
    println(message + ": " + instance.toString)
    instance
  end log

  def RedeemUserError_Program: IO[Unit] = for {
    fib <- IO(println("/Users/drmark/IdeaProjects/PLANE/src/main/scala/CatsIO/DealingWithErrors/RedeemUserError.scala")).start
    l1 = log("time: 9/24/25:", fib.toString)
    _ <- fib.join
  } yield ()

  def fetchUser(userId: Long): IO[String] =
    IO.raiseError(new RuntimeException(s"user $userId missing")).whenA(userId < 0) *>
      IO.pure(s"user-$userId")

  def showProfile(userId: Long): IO[String] =
    fetchUser(userId).redeem(
      err => s"[error] ${err.getMessage}", // failure branch
      ok => s"[ok] profile for $ok" // success branch
    )

  override def run(args: List[String]): IO[ExitCode] = showProfile(1).flatMap(o => IO.println(o)).as(ExitCode.Success)
