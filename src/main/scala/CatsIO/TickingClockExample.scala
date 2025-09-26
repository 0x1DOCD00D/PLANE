////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO

import cats.effect.{ExitCode, IO, IOApp}

import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success, Try}

object TickingClockExample extends IOApp:
  def log[T](message: String, instance: T): T =
    println(message + ": " + instance.toString)
    instance
  end log

  def TickingClockExample_Program: IO[Unit] = for {
    fib1 <- IO(println("/Users/drmark/IdeaProjects/PLANE/src/main/scala/CatsIO/TickingClockExample.scala")).start
    fib2 <- IO(println(System.currentTimeMillis)).start
    l1 = log("time: 9/25/25:", fib1.toString)
    l2 = log("time: 9/25/25:", fib2.toString)
    _ <- fib1.join
    _ <- fib2.join
  } yield ()

  def printableDate(msTime: Long): String =
    val date = new java.util.Date(msTime)
    val sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    sdf.format(date)

  end printableDate
  val tickingClock: IO[Unit] =
    for {
      _ <- IO.sleep(1.second)
      _ <- IO(println(printableDate(System.currentTimeMillis)))
      _ <- tickingClock
    } yield ()


  override def run(args: List[String]): IO[ExitCode] =
    TickingClockExample_Program >> tickingClock.as(ExitCode.Success)
