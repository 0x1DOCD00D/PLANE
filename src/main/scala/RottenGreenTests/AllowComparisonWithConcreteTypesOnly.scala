////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package RottenGreenTests

object AllowComparisonWithConcreteTypesOnly:
  import scala.compiletime.summonInline

  // A sealed trait for "my custom sum" so it never becomes a plain Int automatically
  sealed trait MySum

  // A data constructor for a sum of two Ints
  sealed trait Plus[A <: Int, B <: Int] extends MySum

  // A custom operator that *does not* reduce at compile time.
  // So +++[1,3] is literally the type Plus[1,3], not 4.
  type +++[A <: Int, B <: Int] = Plus[A, B]

  // ---------------------------------------------
  // (Optional) A separate "normalization" step.
  // If you call Normalize[+++[A,B]], you get the real sum (an Int).
  // But this is *only* applied when you actually want to unify them as numbers.
  // ---------------------------------------------
  import scala.compiletime.ops.int.+  // We'll use this *only* inside Normalize.
  type Normalize[S <: MySum] <: Int = S match
    case Plus[a, b] => a + b

  @main def run(): Unit =
    // 1) Distinct computed sums:
    //    summonInline[+++[1,3] =:= +++[2,2]]
    //    ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    //    Fails to compile, because these remain
    //    Plus[1,3] vs. Plus[2,2], which are different types.

    // Uncomment the line below to see the error:
    //summonInline[+++[1,3] =:= +++[2,2]]

    // 2) If you DO want to treat them as the same numeric value, then normalize:
    type Norm13 = Normalize[+++[1,3]] // becomes 4
    type Norm22 = Normalize[+++[2,2]] // also becomes 4
    summonInline[Norm13 =:= Norm22]    // compiles fine, both are 4

    println("Program compiled and ran successfully!")
