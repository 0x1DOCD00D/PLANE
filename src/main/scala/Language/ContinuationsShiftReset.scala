
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Language

object ContinuationsShiftReset:
  class Shift[+A, -B, +C](val fun: (A => B) => C) {
    def map[A1](f: A => A1): Shift[A1, B, C] = {
      println("In shift's map now")
      new Shift((k: A1 => B) => fun { (x: A) =>
        k(f(x))
      })
    }

    def flatMap[A1, B1, C1 <: B](f: A => Shift[A1, B1, C1]): Shift[A1, B1, C] = {
      println("In shift's flatMap now")
      new Shift((k: A1 => B1) =>
        fun { (x: A) => f(x).fun(k) })
    }
  }

  def reset[A, C](c: Shift[A, A, C]): C =
    println(s"Doing reset on $c")
    c.fun((x: A) => x)

  @main def runContinuation: Unit =
    val ctx: Shift[Int, Int, Int] = for {
      x <- Shift((k: Int => Int) => k(k(k(7))))
    } yield {
      println("In yield now")
      x + 1
    }

    val computationWithLists: List[String] = for {
      a <- List("a", "b", "c")
      b <- List(1, 2, 3)
    } yield {
      println("In list yield now")
      a + b.toString
    }

/*
    println(computationWithLists)
    println(reset(ctx)) // => 10
    println(reset(ctx)) // => 10
*/
