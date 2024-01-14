/*
 * Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package TypeFP
//https://stackoverflow.com/questions/75227132/in-scala-3-how-to-replace-general-type-projection-that-has-been-dropped

/*
trait Entity:
  type Key

type Dictionary[T <: Entity] = Map[T#Key, T]
* */
object TypeMatchProjection:
  trait Entity:
    type Key

  object Entity:
    type Aux[K] = Entity {type Key = K}

  // match type
  type EntityKey[T <: Entity] =
    T match
      case Entity.Aux[k] => k

  type Dictionary[T <: Entity] = Map[EntityKey[T], T]
  @main def runtype = println("TypeMatchProjection")