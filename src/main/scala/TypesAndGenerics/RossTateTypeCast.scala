/*
 *
 *  * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package TypesAndGenerics

object RossTateTypeCast extends App {

  trait A {
    type L <: Nothing
  }

  trait B {
    type L >: Any
  }

  def toL(b: B)(x: Any): b.L = x

  val p: B with A = null
  val p1: B = null

  //  println(toL(p)("somestring").getClass.toString)
  //  println(toL(p)("somestring"):Nothing)
  //  println(toL(null)("somestring").asInstanceOf[Nothing])
  println(toL(p)("somestring").asInstanceOf[Nothing])

  def cast[T, U](x: T): U = toL(p)(x)

  println(cast[Int, String](1) + "somestring")
}
