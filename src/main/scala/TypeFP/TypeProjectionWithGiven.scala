/*
 * Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package TypeFP

//https://stackoverflow.com/questions/75355407/scala-3-typed-tuple-zipping

object TypeProjectionWithGiven:
  trait Entity:
    type Key

  // type class
  trait EntityKey[T <: Entity]:
    type Out

  object EntityKey:
    type Aux[T <: Entity, Out0] = EntityKey[T] {type Out = Out0}

    given [K]: EntityKey.Aux[Entity {type Key = K}, K] = null

  // replacing the type with a trait
  trait Dictionary[T <: Entity](using val entityKey: EntityKey[T]):
    type Dict = Map[entityKey.Out, T]
  @main def runitTP = println("Type projection with given")