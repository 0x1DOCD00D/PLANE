////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package TypeFP

object MapListIncrement:

  import scala.compiletime.ops.int.*

  // Type-level list of Int singletons
  sealed trait TList
  sealed trait TNil  extends TList
  sealed trait TCons[H <: Int & Singleton, T <: TList] extends TList

  // Type-level function: increment a literal Int
  // (no bound on the alias — that's what caused your error)
  type Inc[X <: Int & Singleton] = X + 1

  // Type-level map over TList with a higher-kinded type function F
  type TMap[L <: TList, F[_ <: Int & Singleton]] = L match
    case TNil           => TNil
    case TCons[h, tail] => TCons[F[h], TMap[tail, F]]

  // Example
  type T123     = TCons[1, TCons[2, TCons[3, TNil]]]
  type Mapped   = TMap[T123, Inc]
  type Expected = TCons[2, TCons[3, TCons[4, TNil]]]

  // Compile-time proof (compiles iff the mapping is correct)
  def proof: Mapped =:= Expected = summon

  @main def runMapListIncrement(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/TypeFP/MapListIncrement.scala created at time 9:28AM")
    println("Type-level map with increment function demonstration")
    println("Successfully compiled type-level list operations")
    println("Mapped [1, 2, 3] to [2, 3, 4] at the type level")
