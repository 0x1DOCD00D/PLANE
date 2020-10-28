/*
 *
 *  * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package TypeFP

import shapeless.test.illTyped

object BooleanAlgebra extends App {

  sealed trait BooleanType {
    type ~ <: BooleanType
    type ||[other <: BooleanType] <: BooleanType
    type &&[other <: BooleanType] <: BooleanType
    type xor[other <: BooleanType] <: BooleanType
  }

  trait BooleanTrue extends BooleanType {
    //override is optional when the parent entity is abstract in scala
    override type ~ = BooleanFalse
  }

  trait BooleanFalse extends BooleanType {
    override type ~ = BooleanTrue
  }

  type \/[A <: BooleanType, B <: BooleanType] = A#`||`[B]

  //  implicitly[T](implicit e: T): T = e
  //  abstract class =:=[From, To] extends (From <:< To)
  val res = implicitly[BooleanTrue =:= BooleanTrue]
  println(res.getClass.toString())

  illTyped("implicitly[BooleanTrue =:= BooleanFalse]")
}
