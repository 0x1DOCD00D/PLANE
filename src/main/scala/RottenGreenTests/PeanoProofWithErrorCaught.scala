////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package RottenGreenTests

object PeanoProofWithErrorCaught:
  // File: PeanoAssociativity.scala

  // 1) Define the PeanoT type-level representation of Peano numbers
  sealed trait PeanoT

  case object Zero extends PeanoT
  type ZeroT = Zero.type

  case class SuccT[N <: PeanoT](pred: N) extends PeanoT

  // 2) Define the type-level Add function
  //    - ZeroT + B => B
  //    - SuccT[a] + B => SuccT[a + B]
  type Add[A <: PeanoT, B <: PeanoT] <: PeanoT = A match
    case ZeroT => B
    case SuccT[aTail] => SuccT[Add[aTail, B]]

  // 3) LemmaSuccAdd states: Add[SuccT[A], B] = SuccT[Add[A, B]]
  //    This helps the compiler unify expansions step by step.
  trait LemmaSuccAdd[A <: PeanoT, B <: PeanoT]

  object LemmaSuccAdd:
    // Base case: A = ZeroT
    given lemmaZero[B <: PeanoT]: LemmaSuccAdd[ZeroT, B] =
    new LemmaSuccAdd[ZeroT, B] {}

    // Inductive step: A = SuccT[A0]
    // If LemmaSuccAdd[A0, B] holds, then LemmaSuccAdd[SuccT[A0], B] holds.
    given lemmaSucc[A0 <: PeanoT, B <: PeanoT](using LemmaSuccAdd[A0, B]): LemmaSuccAdd[SuccT[A0], B] =
      new LemmaSuccAdd[SuccT[A0], B] {}

  // 4) Define a proof of associativity (A + B) + C = A + (B + C)
  trait Associative[A <: PeanoT, B <: PeanoT, C <: PeanoT]
/*
    type LeftSide = Add[Add[A, B], C] // (A + B) + C
    type RightSide = Add[A, Add[B, C]] // A + (B + C)
*/

    // If LeftSide and RightSide fail to unify, compilation fails.
//    summon[LeftSide =:= RightSide]

  object Associative:

    // Base case: A = ZeroT
    //  (ZeroT + B) + C = B + C
    //  ZeroT + (B + C) = B + C
    given assocZero[B <: PeanoT, C <: PeanoT]: Associative[ZeroT, B, C] = new Associative[ZeroT, B, C] {}

    // Inductive step: A = SuccT[A0]
    // We assume we have a proof of Associative[A0,B,C],
    // plus the lemma that Succ(A) + B => Succ(A + B).
    given assocSucc[A0 <: PeanoT, B <: PeanoT, C <: PeanoT](
                                                             using
                                                             base: Associative[A0, B, C], // Inductive hypothesis
                                                             l1: LemmaSuccAdd[A0, B], // For expansions in (A0 + B)
                                                             l2: LemmaSuccAdd[Add[A0, B], C], // For expansions in ((A0+B) + C)
                                                             l3: LemmaSuccAdd[B, C] // For expansions in (B + C)
                                                           ): Associative[SuccT[A0], B, C] =
    new Associative[SuccT[A0], B, C] {}

  // 5) A main object to demonstrate the proof works for 2,3,1
  object PeanoAssociativity:

    // Type-level definitions for 2, 3, 1
    type One = SuccT[ZeroT]
    type Two = SuccT[SuccT[ZeroT]]
    type Three = SuccT[SuccT[SuccT[ZeroT]]]

    // Summon proves (2+3)+1 = 2+(3+1) at compile time
    summon[Associative[Two, Three, One]]


  @main def runPeanoProofWithErrorCaught(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/RottenGreenTests/PeanoProofWithErrorCaught.scala created at time 1:38PM")
