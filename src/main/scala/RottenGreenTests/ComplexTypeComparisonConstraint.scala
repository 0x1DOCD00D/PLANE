////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package RottenGreenTests

object ComplexTypeComparisonConstraint:
  import scala.compiletime.ops.int.+
  import scala.compiletime.summonInline

  // 1) A sealed ADT for your expressions:
  sealed trait MyExpr
  sealed trait MySum[A <: Int, B <: Int] extends MyExpr
  sealed trait MyConstVal[A <: Int] extends MyExpr

  // 2) Convenience type aliases:
  type +++[A <: Int, B <: Int] = MySum[A, B]
  type K[A <: Int] = MyConstVal[A]

  // 3) A typeclass controlling which pairs *may* be compared:
  trait Allowed[E1 <: MyExpr, E2 <: MyExpr]

  //   - Summation vs. Const => Allowed
  given sumConst[A <: Int, B <: Int, C <: Int]: Allowed[MySum[A, B], MyConstVal[C]] with {}

  //   - Const vs. Const => Allowed
  given constConst[A <: Int, B <: Int]: Allowed[MyConstVal[A], MyConstVal[B]] with {}

  //   - NO Summation vs Summation => Not given => disallowed

  // 4) A type-level Evaluate function to get the final numeric value:
  //    Summation[A,B] => A + B
  //    MyConstVal[X] => X
  type Evaluate[E <: MyExpr] <: Int = E match
    case MySum[a, b]   => a + b
    case MyConstVal[x] => x

  // 5) Compare two expressions *by their final integer values*.
  //    We unify Evaluate[E1] and Evaluate[E2], not E1 and E2 themselves.
  inline def compareByValue[E1 <: MyExpr, E2 <: MyExpr](using allow: Allowed[E1, E2]): Evaluate[E1] =:= Evaluate[E2] =
    summonInline[Evaluate[E1] =:= Evaluate[E2]]
  // If Evaluate[E1] and Evaluate[E2] both reduce to the same Int, this compiles.
  // If either there's no Allowed[..], or the sums differ, this fails.

  @main def runCompareExpToConcreteType(): Unit =
    // Summation(1,3) => Evaluate => 4
    // MyConstVal(4) => Evaluate => 4
    // => unify => compile-time success
    val ev1 = compareByValue[+++[1, 3], K[4]]
    println("compareByValue[+++[1,3], K[4]] => " + ev1)

    // Constant vs Constant => Allowed
    // Evaluate[K[4]] => 4, Evaluate[K[4]] => 4 => unify => success
    val ev2 = compareByValue[K[4], K[4]]
    println("compareByValue[K[4], K[4]] => " + ev2)

    // Summation vs Summation => Not allowed => fails if uncommented:
//    val ev3 = compareByValue[+++[1,3], +++[2,2]]

    // Summation vs a different constant => numeric mismatch => fails to unify 4 =:= 5
    // val ev4 = compareByValue[+++[1,3], K[5]]

    println("Done!")
