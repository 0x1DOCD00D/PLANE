
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Language

object MyTypeComprehensionMonad:
  case class MyType[A](value: A):
    def map[B](f: A => B): MyType[B] = MyType(f(value))

    def flatMap[B](f: A => MyType[B]): MyType[B] = f(value)

    def withFilter(p: A => Boolean): MyType[A] =
      if (p(value)) this
      else throw new NoSuchElementException("Predicate does not hold for value")

    def foreach(f: A => Unit): Unit = f(value)

  @main def runMyTypeComprehensionMonad(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Language/MyTypeComprehensionMonad.scala created at time 2:43PM")
    val instance1 = MyType(10)
    val instance2 = MyType(20)
    val result = for {
      x <- instance1 // Generator 1
      y <- instance2 // Generator 2
      if y > 15 // Guard
    } yield x + y // Yielded expression
    println(result)
