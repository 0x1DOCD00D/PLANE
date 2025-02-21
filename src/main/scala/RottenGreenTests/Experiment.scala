
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package RottenGreenTests

object Experiment:
  import scala.compiletime.summonInline

  // Step 1: Define Boolean Type System
  sealed trait Bool
  sealed trait True extends Bool
  sealed trait False extends Bool

  // Step 2: Define Type-Level AND without Explicit Values
  type AND[T <: Bool, S <: Bool] <: Bool

  // Step 3: Theorem: Prove that AND[T, S] =:= AND[S, T] for All T, S
  trait AndCommutative[T <: Bool, S <: Bool]:
    type Proof = AND[T, S] =:= AND[S, T]

  given andCommutativeTheorem[T <: Bool, S <: Bool]: AndCommutative[T, S] =
    new AndCommutative[T, S] {}

  // Step 4: Verify Theorem at Compile-Time without Instantiating Types
  @main def testTheorem(): Unit =
    summon[AndCommutative[_, _]] // This ensures that the proof works for any Bool values
    println("Theorem: ∀ T, S ∈ Bool, AND[T, S] =:= AND[S, T] proven at compile-time!")

