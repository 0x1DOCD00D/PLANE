////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package TypeFP

object CompareStringsTypeLevel:
  type StringsAreEqualA[S1 <: String, S2 <: String] =
    compiletime.ops.any.==[S1, S2]

  type StringsAreEqualB[S1 <: String, S2 <: String] <: Boolean = S1 match
    case S2 => true
    case _  => false

  type X1A = StringsAreEqualA["lol", "lol"]
  type X2A = StringsAreEqualA["lol", "wat"]
  type X1B = StringsAreEqualB["lol", "lol"]
  type X2B = StringsAreEqualB["lol", "wat"]

  val x1a: X1A = compiletime.constValue[X1A]
  val x2a: X2A = compiletime.constValue[X2A]
  val x1b: X1B = compiletime.constValue[X1B]
  val x2b: X2B = compiletime.constValue[X2B]

  println(x1a) // prints true
  println(x2a) // prints false
  println(x1b) // prints true
  println(x2b) // prints false

  @main def runCompareStringsTypeLevel(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/TypeFP/CompareStringsTypeLevel.scala created at time 10:12AM")
