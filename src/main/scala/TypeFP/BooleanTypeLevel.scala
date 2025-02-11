////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package TypeFP

object BooleanTypeLevel:

  import scala.compiletime.{erasedValue, summonInline}

  // Type-level Boolean definitions
  sealed trait Bool

  sealed trait True extends Bool

  sealed trait False extends Bool

  object Bool:
    // Type-level NOT
    type ^[A <: Bool] <: Bool = A match
      case True  => False
      case False => True

    // Type-level AND
    type /\[A <: Bool, B <: Bool] <: Bool = A match
      case True  => B
      case False => False

    // Type-level OR
    type \/[A <: Bool, B <: Bool] <: Bool = A match
      case True  => True
      case False => B

    // De Morgan's laws: Type equivalence definitions
    type DeMorganAnd[A <: Bool, B <: Bool] = ^[(A \/ B)] =:= (^[A] /\ ^[B])
    type DeMorganOr[A <: Bool, B <: Bool] = ^[(A /\ B)] =:= (^[A] \/ ^[B])

    // Type class to inductively prove De Morgan’s laws
    trait DeMorganProof[A <: Bool, B <: Bool]

    object DeMorganProof:
      given dmTrueFalse: DeMorganProof[True, False] = new DeMorganProof[True, False] {}

      given dmFalseTrue: DeMorganProof[False, True] = new DeMorganProof[False, True] {}

      given dmFalseFalse: DeMorganProof[False, False] = new DeMorganProof[False, False] {}

      given dmTrueTrue: DeMorganProof[True, True] = new DeMorganProof[True, True] {}

    // Summon proof at compile-time
    inline def verifyDeMorganAnd[A <: Bool, B <: Bool](using DeMorganProof[A, B]): DeMorganAnd[A, B] = summonInline

    inline def verifyDeMorganOr[A <: Bool, B <: Bool](using DeMorganProof[A, B]): DeMorganOr[A, B] = summonInline

  @main def runBooleanTypeLevel(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/TypeFP/BooleanTypeLevel.scala created at time 8:01PM")
    // Verify De Morgan’s Laws at the type level
    val dmAndTrueFalse = Bool.verifyDeMorganAnd[True, False] // Should compile
    val dmOrTrueFalse = Bool.verifyDeMorganOr[True, False] // Should compile

    val dmAndFalseFalse = Bool.verifyDeMorganAnd[False, False] // Should compile
    val dmOrFalseFalse = Bool.verifyDeMorganOr[False, False] // Should compile

    val dmAndTrueTrue = Bool.verifyDeMorganAnd[True, True] // Should compile
    val dmOrTrueTrue = Bool.verifyDeMorganOr[True, True] // Should compile

    println("De Morgan's laws verified at the type level!")
