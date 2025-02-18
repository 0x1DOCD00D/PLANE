////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scala3Features

import scala.compiletime.ops.int.+

object SizedListCompileTimeProof:
  sealed trait SizedList[A, N <: Int] // N is a type-level natural

  object SizedList:
    case class Nil[A]() extends SizedList[A, 0]

    case class Cons[A, N <: Int](head: A, tail: SizedList[A, N]) extends SizedList[A, N + 1]

  inline def proveNonEmpty[A, N <: Int](xs: SizedList[A, N]): Unit =
    inline if valueOf[N] == 0 then
      compiletime.error("List is empty!")
    else
      (
    )

  @main def runSizedList(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/scala3Features/SizedListCompileTimeProof.scala created at time 8:34PM")
    val xs = SizedList.Cons(1, SizedList.Cons(2, SizedList.Cons(3, SizedList.Nil())))
    proveNonEmpty(xs)
//    this code fails
//    proveNonEmpty(SizedList.Nil())
