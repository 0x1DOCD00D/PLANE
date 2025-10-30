////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package TypeFP

import TypeFP.MapListIncrement.TApply.Aux

import scala.runtime.stdLibPatches.Predef.summon

object MapListIncrement:
  import scala.compiletime.ops.int.*

  // Type-Level List Definition
  sealed trait TList

  sealed trait TNil extends TList

  sealed trait TCons[H, T <: TList] extends TList

  type One = 1
  type Two = 2
  type Three = 3
  type Four = 4

  // Type-Level Function Application Trait
  trait TApply[F, X]:
    type Out

  object TApply:
    type Aux[F, X, O] = TApply[F, X] { type Out = O }

  // Type-Level Map
  trait TMap[L <: TList, F]:
    type Out <: TList

  object TMap:
    type Aux[L <: TList, F, O <: TList] = TMap[L, F] { type Out = O }

    // 1) Base Case: Mapping TNil results in TNil
    given mapNil[F]: TMap.Aux[TNil, F, TNil] =
      new TMap[TNil, F]:
        type Out = TNil

    // 2) Recursive Case: Mapping TCons[H, T]
    given mapCons[H <: Int, T <: TList, F, HO <: Int, TO <: TList](using
       headApp: TApply.Aux[F, H, HO], // Apply function to H
       tailMap: TMap.Aux[T, F, TO] // Recursively apply to T
    ): TMap.Aux[TCons[H, T], F, TCons[HO, TO]] =
      new TMap[TCons[H, T], F]:
        type Out = TCons[HO, TO]

  // A Type-Level Increment Function: TInc
  sealed trait TInc

  given [X <: Int & Singleton]: TApply.Aux[TInc, X, X + 1] =
    new TApply[TInc, X]:
      type Out = X + 1

  def proof: TApply.Aux[TInc, 1, 2] = summon[TApply[TInc, 1]]

  // Example Usage
  type T123 = TCons[One, TCons[Two, TCons[Three, TNil]]] // Type-Level List: [1, 2, 3]

  // Apply `TInc` to every element in T123
  type Mapped = TMap[T123, TInc]#Out

  // The expected result: [2, 3, 4]
  type Expected = TCons[Two, TCons[Three, TCons[Four, TNil]]]

  import scala.compiletime.*

  inline def printType[T]: Unit = println(summon[T =:= T])

  def checkTMap: TMap.Aux[TCons[1, TCons[2, TCons[3, TNil]]], TInc, TCons[2, TCons[3, TCons[4, TNil]]]] =
    summon[TMap[TCons[1, TCons[2, TCons[3, TNil]]], TInc]]

  def checkTApply1: Aux[TInc, 1, 2] = summon[TApply[TInc, 1]]

  def checkTApply2: Aux[TInc, 2, 3] = summon[TApply[TInc, 2]]

  def checkTApply3: Aux[TInc, 3, 4] = summon[TApply[TInc, 3]]

//  def checkTMap1: TMap.Aux[TCons[1, TCons[2, TCons[3, TNil]]], TInc, TCons[2, TCons[3, TCons[4, TNil]]]] = summon[TMap[TCons[1, TCons[2, TCons[3, TNil]]], TInc]]
  def checkTMap1: TMap.Aux[TCons[1, TCons[2, TCons[3, TNil]]], TInc, TCons[2, TCons[3, TCons[4, TNil]]]] = summon[TMap[TCons[1, TCons[2, TCons[3, TNil]]], TInc]]

  type FullyExpandedMapped = TMap[TCons[1, TCons[2, TCons[3, TNil]]], TInc]#Out
  type FullyExpandedExpected = TCons[2, TCons[3, TCons[4, TNil]]]

  type ResolvedMapped = TMap[TCons[1, TCons[2, TCons[3, TNil]]], TInc] match
    case t: TMap[_, _] => t.Out

  def checkTMap2: TMap.Aux[TCons[2, TCons[3, TNil]], TInc, TCons[3, TCons[4, TNil]]] = summon[TMap[TCons[2, TCons[3, TNil]], TInc]]

  def checkTMap3: TMap.Aux[TCons[3, TNil], TInc, TCons[4, TNil]] = summon[TMap[TCons[3, TNil], TInc]]

  def checkTMapNil: TMap.Aux[TNil, TInc, TNil] = summon[TMap[TNil, TInc]]

  def checkTApply: Aux[TInc, 1, 2] = summon[TApply[TInc, 1]]

  type ExtractOut[X] = X match
    case t: TApply[_, _] => t.Out
    case _               => X

  type ExtractedTApply = ExtractOut[Aux[TInc, 1, 2]]
  type Expected2 = 2

  def proof1 = summon[ExtractedTApply =:= Expected2]

  def proof2 =
//    : Aux[TInc, 1, 2]
    val r = summon[TApply[TInc, 1]]
    summon[ExtractOut[r.type] =:= Expected2]

  @main def runMapListIncrement(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/TypeFP/MapListIncrement.scala created at time 9:28AM")
