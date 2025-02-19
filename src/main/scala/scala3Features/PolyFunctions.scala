
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scala3Features

object PolyFunctions:
  val idF: [T] => T => T = [T] => (x: T) => x
  val intIdentity = idF(58)
  val stringIdentity = idF("Mark")

  val mapPoly: [A, B] => (List[A], A => B) => List[B] =
    [A, B] => (list: List[A], f: A => B) => list.map(f)

  import scala.util.Try

  val safeCast: [A, B] => A => Option[B] =
    [A, B] => (a: A) => Try(a.asInstanceOf[B]).toOption

  val curryPoly: [A, B, C] => (A => B => C) => (A, B) => C =
    [A, B, C] => (f: A => B => C) => (a: A, b: B) => f(a)(b)

  val mergeContainers: [F[_] <: Iterable[A], A] => (F[A], F[A]) => List[A] =
    [F[_] <: Iterable[A], A] => (fa: F[A], fb: F[A]) => fa.toList ++ fb.toList


  @main def runPolyFunctions(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/scala3Features/PolyFunctions.scala created at time 8:02PM")
    println(s"intIdentity: $intIdentity and stringIdentity: $stringIdentity")
    println {
      List(1, 2, 3).map(idF[Int]).mkString(", ") + " --> " +
      mapPoly(List(1, 2, 3), (x: Int) => x + 1).mkString(", ")
    }
    val num: Option[Int] = safeCast[Any, Int]("hello")
    val validCast: Option[String] = safeCast[Any, String]("Scala")
    println(s"num: $num and validCast: $validCast")
    val add: Int => Int => Int = a => b => a + b
    val curriedAdd = curryPoly(add)
    val sum = curriedAdd(2, 3)
    println(s"sum: $sum")
    val mergedList = mergeContainers(List(1, 2, 3), List(4, 5, 6))
    println(s"mergedList: $mergedList")




