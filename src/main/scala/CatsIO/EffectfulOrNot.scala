
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO

import cats.effect.{ExitCode, IO, IOApp}

import scala.util.{Failure, Success, Try}
import scala.concurrent.*
import ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt

/*
a is not assigned the value `()`.
a is assigned a Future[Unit] that has already been started. That future will eventually complete with ().

Evaluating the RHS submits `println("a")` to the `ExecutionContext` immediately.
The side effect runs once (asynchronously).
a now refers to a started Future[Unit] that will complete with ().

Your comprehension val p = for { _ <- a; _ <- a } yield ()

desugars to

val p = a.flatMap(_ => a.map(_ => ()))

Both _ <- a steps just await the same future a. They do not re-run the body println("a"), because that body ran when a was created.
So you see “a” once.
* */
val a: Future[Unit] = Future(println("a"))  // runs now

object EffectfulOrNot extends IOApp:
  def log[T](message: String, instance: T): T =
    println(message + ": " + instance.toString)
    instance
  end log

  def simpleEffectful: IO[Int] = IO {
    println("simpleEffectful")
    42
  }

  def hitOnce: Future[Any] = for {
    _ <- a
    _ <- a
    _ <- a
    _ <- a
    _ <- a
  } yield ()

  def fiveHits: Future[Any] = for {
    _ <- Future(println("a"))
    _ <- Future(println("a"))
    _ <- Future(println("a"))
    _ <- Future(println("a"))
    _ <- Future(println("a"))
  } yield ()


  def EffectfulOrNot_Program: IO[Unit] = for {
    fib <- IO.sleep(5.seconds) *> IO(println("/Users/drmark/IdeaProjects/PLANE/src/main/scala/CatsIO/EffectfulOrNot.scala")).start
    l1 = log("time: 9/24/25:", fib.toString)
    _ <- fib.join
  } yield ()

  override def run(args: List[String]): IO[ExitCode] =
//    Await.result(a, duration.Duration.Inf)  // ensure a runs before anything else
    hitOnce
    fiveHits

    simpleEffectful *> simpleEffectful *> EffectfulOrNot_Program.guarantee(IO.println("shutting down")).as(ExitCode.Success)
