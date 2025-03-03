////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package RottenGreenTests

object PeanoTypeLevelProofVanilla:
  sealed trait Peano
  case object Zero extends Peano
  type ZeroT = Zero.type
  case class Succ[N <: Peano](n: N) extends Peano
  trait Sum[A <: Peano, B <: Peano, C <: Peano]
  object Sum:
    given zeroSum[B <: Peano]: Sum[ZeroT, B, B] = new Sum[ZeroT, B, B] {}
    given succSum[A <: Peano, B <: Peano, C <: Peano]
        (using sum: Sum[A, B, C]): Sum[Succ[A], B, Succ[C]] =
            new Sum[Succ[A], B, Succ[C]] {}

  type Two = Succ[Succ[ZeroT]]
  type Three = Succ[Succ[Succ[ZeroT]]]
  type Five = Succ[Succ[Succ[Succ[Succ[ZeroT]]]]]
  type Six = Succ[Succ[Succ[Succ[Succ[Succ[ZeroT]]]]]]
  summon[Sum[Two, Three, Five]]
//  summon[Sum[Two, Three, Six]]

  @main def runPeanoTypeLevelProofVanilla(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/RottenGreenTests/PeanoTypeLevelProofVanilla.scala created at time 8:04PM")
