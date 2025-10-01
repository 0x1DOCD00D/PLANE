
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.Concepts

import cats.effect.{ExitCode, IO, IOApp}

import scala.util.{Failure, Success, Try}
import cats.effect.*
import cats.implicits.*
import CatsIO.Helpers.Aid4Debugging.*
import CatsIO.Helpers.{blue, bold, green, red}

import java.util.concurrent.{Executors, ThreadFactory, TimeUnit}
import scala.concurrent.duration.DurationInt

object FormingCalls extends IOApp:
  val output: IO[Unit] = IO.println("No output is produced.")

  val printOut: String => IO[Unit] = (out: String) => IO.println(out)

  val askAndRead: IO[String] = IO.print("Enter your name: ") >> IO.readLine

  private val daemonTF: ThreadFactory = (r: Runnable) => {
    val t = new Thread(r)
    t.setDaemon(true) // <- daemon so it won't block JVM exit
    t.setName(s"scheduler-daemon-${t.getName}")
    t
  }
  private val schedulerExecutor = Executors.newScheduledThreadPool(1, daemonTF)
  def log[T](message: String, instance: T): T =
    println(message + ": " + instance.toString)
    instance
  end log

  def FormingCalls_Program: IO[Unit] = for {
    fib <- IO(println("/Users/drmark/IdeaProjects/PLANE/src/main/scala/CatsIO/Concepts/FormingCalls.scala")).start
    l1 = log("time: 10/1/25:", fib.toString)
    _ <- fib.join
  } yield ()

  override def run(args: List[String]): IO[ExitCode] =
    lazy val loop: IO[Unit] = IO.println("Hello, World!") >> IO.sleep(2.seconds) >> loop

    val asyncUnit: IO[Unit] = IO.async_[Unit] { cb =>
      schedulerExecutor.schedule(new Runnable {
        def run = cb {
          println("[timeout] inside the callback".blue.bold)
          Right(())
        }
      }, 1500, TimeUnit.MILLISECONDS)
      ()
    }
    (askAndRead >>= printOut).debugInfo() *>
    asyncUnit.debugInfo() >> loop.timeout(5.seconds).debugInfo().handleErrorWith {
        case e: Throwable => IO.println(s"Computation timed out: ${e.getMessage}".red.bold)
    } >>
    IO.println("static stuff").debugInfo() *>
      IO.println("computing stuff").debugInfo() >>
      output.flatMap(_=>IO.println("from no output to some output!".green)).debugInfo() *>
    FormingCalls_Program.as(ExitCode.Success)
