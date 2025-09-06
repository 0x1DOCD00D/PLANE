
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.Evals

object EvalMonadDefer:

  import cats.Eval

  def safeSum(xs: List[Int]): Eval[Long] =
    xs match
      case Nil => Eval.now(0L)
      case h :: t => Eval.defer(safeSum(t).map(_ + h))

  def notSafeSum(xs: List[Int]): Long =
    xs match
      case Nil => 0L
      case h :: t => notSafeSum(t) + h

  val bigList: List[Int] = (1 to 100000000).toList
  val result1: Long = notSafeSum(bigList) // this overflows the stack
  val result: Long = safeSum(bigList).value
  println(result)

  @main def runEvalMonadDefer(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Cats/EvalMonadDefer.scala created at time 2:42PM")
    println(s"the result is $result")
