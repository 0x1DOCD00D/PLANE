
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Language

object ContinuationsComprehensionTemplate:
  case class Cont[R, A](run: (A => R) => R) {
    def flatMap[B](f: A => Cont[R, B]): Cont[R, B] = Cont { k => run(a => f(a).run(k)) }

    def map[B](f: A => B): Cont[R, B] = flatMap(a => Cont(k => k(f(a))))
  }

  @main def runContinuationsComprehensionTemplate(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Language/ContinuationsComprehensionTemplate.scala created at time 4:13PM")
    val double: Cont[String, Int] = Cont[String, Int](k => k(10 * 2)) // Produces 20
    val addFive: Int => Cont[String, Int] = x => Cont(k => k(x + 5)) // Produces x + 5

    val computation = for {
      x <- double // `x = 20`
      y <- addFive(x) // `y = x + 5 = 25`
    } yield y // `y = 25`

    val result = computation.run(x => s"Final result: $x")
    println(result)