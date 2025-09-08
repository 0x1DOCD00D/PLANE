
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.ReaderWriter

object WriterForFinJob:
  import cats.data.Writer
  import cats.implicits._

  type Logged[A] = Writer[List[String], A]

  def step1(amount: Double): Logged[Double] =
    Writer(List(s"Starting with $amount"), amount)

  def step2(amount: Double): Logged[Double] =
    Writer(List(s"Applying tax to $amount"), amount * 1.1)

  def step3(amount: Double): Logged[Double] =
    Writer(List(s"Applying discount to $amount"), amount * 0.9)

  /*
  The for-comprehension chains the steps together, passing the result of one step to the
  next.deposit(0, 500).flatMap { b1 =>
    withdraw(b1, 200).flatMap { b2 =>
      interest(b2, 0.05).map { b3 =>
        b3
      }
    }
  }
  or in our case
  val program: Logged[Double] =
    step1(100.0).flatMap { a1 =>
      step2(a1).flatMap { a2 =>
        step3(a2).map { a3 =>
          a3
        }
      }
    }
  where
    def flatMap[B](f: A => Writer[L, B])(using m: Monoid[L]): Writer[L, B] =
      Writer {
        val (log1, a) = this.run       // log and value from first Writer
        val (log2, b) = f(a).run       // log and value from applying f
        (log1 |+| log2, b)             // combine logs, keep new value
      }
  * */
  val program: Logged[Double] = for {
//    Writer(List("Starting with 100.0"), 100.0)
    /*
    log = List("Starting with 100.0")
    value = 100.0
    this value is bound to a1
    * */
    a1 <- step1(100.0)
    /*
    a1 <- step1(100.0) then step2(a1)
    a1 = 100.0
    step2(100.0) returns: Writer(List("Applying tax to 100.0"), 110.0)
    log = List("Starting with 100.0") |+| List("Applying tax to 100.0")
    (List("Starting with 100.0") ++ List("Applying tax to 100.0"), 110.0)
    * */
    a2 <- step2(a1)
    a3 <- step3(a2)
  } yield a3

  @main def runWriterForFinJob(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Cats/ReaderWriter/WriterForFinJob.scala created at time 1:38PM")
    val (log, result) = program.run
    println(s"Result: $result")
    println("Log:")
    log.foreach(println)
