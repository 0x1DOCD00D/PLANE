/*
 * Copyright (c) 2023 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package CatsIO

import cats.effect.IO

object Aid4Debugging:
  def putStrLn[T](value: T): IO[Unit] = IO(println(value.toString))

  def printStackContent: IO[Unit] = IO(Thread.currentThread().getStackTrace.foreach(println))

  def printStackContentEagerly(): Unit =
    println("-----------------------------------------------------")
    Thread.currentThread().getStackTrace.foreach(println)

  def log[T](message: String, instance: T): T =
    println(message + ": " + instance.toString)
    instance
  end log
  
  extension [A](io: IO[A])
    def showThreadAndData: IO[A] = 
      io.map(value => {
        val x = 1
        println(value)
      })
      for {
      someValue <- io
/*
      t = (Thread.currentThread().getName, Thread.currentThread().threadId())
      _ = println(s"Thread [${t._1}, ${t._2}] ${someValue.toString}")
*/
    } yield someValue