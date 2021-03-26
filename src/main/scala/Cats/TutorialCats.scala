/*
 *
 *  * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package Cats

import cats.Show
import cats.implicits._
import simulacrum.{op, typeclass}

//Many examples come from http://eed3si9n.com/herding-cats

object TutorialCats extends App {
  case class X(p: Int)

  case class Y(p: Double)

  implicit val xShow = Show.show[X](_.p.toString)
  implicit val yShow = Show.fromToString[Y]

  println(1 === 1)
  println(1 =!= 1)
  println(2 compare 5)
  println(5 compare 2)
  println(5 compare 5)
  println(5 tryCompare 5)
  //println(5 tryCompare  5.0)//type mismatch
  //  println(1 === "howdy") //the compiler fails the comparison
  //  println(1 == "howdy") //scala equivalence check does not fail at the compilation time
  println("Howdy" === "howdy")
  println(X(100).show)
  println(Y(Math.PI).show)

  //  @typeclass trait Semigroup[A] {
  //    @op("|+|") def append(x: A, y: A): A
  //  }
  //
  //  implicit val semigroupInt: Semigroup[Int] = new Semigroup[Int] {
  //    def append(x: Int, y: Int) = x + y
  //  }
  //
  //  println(1 |+| 2)

  @typeclass trait ConcatIt[A] {
    @op("|~|") def cc(x: A, y: A): String
  }

  implicit val concIt: ConcatIt[String] = new ConcatIt[String] {
    def cc(x: String, y: String): String = x.toCharArray.zip(y.toCharArray).toString
  }

  println("abcd" |+| "efg")

}
