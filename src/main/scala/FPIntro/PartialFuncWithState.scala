/*
 * Copyright (c) 2023 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package FPIntro

object ReturnPartialFunc:
  def apply(y: => Int): PartialFunction[Int, Int] = {
    case x: Int => x + y
    case _ => throw new Exception("Not an integer")
  }
object PartialFuncWithState:
  @main def runPartialFuncWithState(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/FPIntro/PartialFuncWithState.scala created at time 6:06 PM")
    println(ReturnPartialFunc(2)(3))