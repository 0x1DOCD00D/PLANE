////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package RottenGreenTests

object BooleanAndCommutativeProof:
  import scala.compiletime.ops.any.==
  import scala.compiletime.summonInline

  // Step 1: Define Boolean Type System
  sealed trait Bool
  sealed trait True extends Bool
  sealed trait False extends Bool
  sealed trait Maybe extends Bool

  // Step 2: Define AND[T, S] as a Match Type
  type AND[T <: Bool, S <: Bool] <: Bool = (T, S) match
    case (True, True)   => True
    case (True, False)  => False
    case (False, True)  => False
    case (False, False) => False
    // this is an asymetric case that is not commutative
    // so the compilation will fail
    // to make it succeed we need both cases to evaluate to the same result
    case (Maybe, False) => False
    case (False, Maybe) => False

    case (Maybe, True) => False
    case (True, Maybe) => True

  // Step 4: Define Alternative Proof Using Inductive Type Evidence
  trait Commutative[T <: Bool, S <: Bool]:
    def proof: AND[T, S] =:= AND[S, T]

  given commutativeTrueTrue: Commutative[True, True] with
    def proof: AND[True, True] =:= AND[True, True] = summonInline

  given commutativeTrueFalse: Commutative[True, False] with
    def proof: AND[True, False] =:= AND[False, True] = summonInline

  given commutativeFalseTrue: Commutative[False, True] with
    def proof: AND[False, True] =:= AND[True, False] = summonInline

  given commutativeFalseFalse: Commutative[False, False] with
    def proof: AND[False, False] =:= AND[False, False] = summonInline

  given commutativeMaybeFalse: Commutative[Maybe, False] with
    def proof: AND[Maybe, False] =:= AND[Maybe, False] = summonInline

  given commutativeFalseMaybe: Commutative[False, Maybe] with
    def proof: AND[False, Maybe] =:= AND[False, Maybe] = summonInline

  @main def test(): Unit =
    val commProof1 = summon[Commutative[True, False]].proof
    val commProof2 = summon[Commutative[Maybe, False]].proof
