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

import scala.util.Either

object TypesAndLogic {
  /*
    final case class Left[+A, +B](value: A) extends Either[A, B] {
      def isLeft  = true
      def isRight = false

      /**
       * Upcasts this `Left[A, B]` to `Either[A, B1]`
       * {{{
       *   Left(1)                   // Either[Int, Nothing]
       *   Left(1).withRight[String] // Either[Int, String]
       * }}}
       */
      def withRight[B1 >: B]: Either[A, B1] = this

    }

    /** The right side of the disjoint union, as opposed to the [[scala.util.Left]] side.
     */
    final case class Right[+A, +B](value: B) extends Either[A, B] {
      def isLeft  = false
      def isRight = true

      /**
       * Upcasts this `Right[A, B]` to `Either[A1, B]`
       * {{{
       *   Right("x")               // Either[Nothing, String]
       *   Right("x").withLeft[Int] // Either[Int, String]
       * }}}
       */
      def withLeft[A1 >: A]: Either[A1, B] = this

    }
  */


  //union types: T or S
  //  Either is used to model the union of types
  def f[T, S](i: Either[T, S]) = if (i.isLeft) Right(true) else Left(false)

  def returnUnion(i: String): Either[NumberFormatException, Int] = {
    try {
      Right(i.toInt)
    } catch {
      case ex: NumberFormatException => Left(ex)
    }
  }

  returnUnion("not integer string").map(println)

  trait A

  trait B

  //  A and B
  val andType: A with B = new A with B {}

  class C extends A with B

  //  implication => is implemented with subtyping <:
  //  T => A
  def imply[T <: A](i: T) = null

  imply(andType)
  //  inferred type arguments [TypeFP.TypesAndLogic.B] do not conform to method imply's type parameter bounds [T <: TypeFP.TypesAndLogic.A]
  //  imply(new B {})

  def implyOr[T <: Either[A, B]](i: T) = null

  implyOr(Left(new A {}))
  implyOr(Right(new B {}))
  implyOr(Left(new C))
  implyOr(Right(new C))

  /*
  * With negation of some type, T we want to define the set of values that are NOT instances of T
  * not T is the set of types which cannot be used to substitute for T
  * For the bottom type Nothing, no values of any other type can be used to substitute for the type Nothing
  * T or S equiv not((not T) and (not S)) equiv not((not T) with (not S))
  * T => (T or false) equiv T <: Either[T, Nothing]
  * not T equiv T => false using the truth table for NOT
  * */
  type NOT[T] = T => Nothing
  type OR[T, S] = NOT[NOT[T] with NOT[S]]

  //  Let's write the code that proves that Int <:< (Int OR String)
  implicitly[NOT[NOT[Int]] <:< OR[Int, String]]

  //  expanding the type aliases we need to determine how we define subtyping for functions
  trait T1

  trait T2 extends T1

  trait S2

  trait S1 extends S2

  implicitly[Function1[T1, S1] <:< Function1[T2, S2]]

  trait Human

  trait American extends Human

  trait Democrat

  trait Progressive extends Democrat

  /*
  * Consider two functions: f1: Function1[Human,Progressive] and f2: Function1[American,Democrat]
  * All inputs for f2 can be substituted for the inputs of f1, since every American is a Human.
  * However, inputs for f1 can NOT be substituted for the inputs of f2, since not every Human is an American
  * It means that f1 can be applied to all inputs of Human including those of American.
  * The output of f1, Progressive can be substituted for the output of f2, Democrat, not the other way around.
  * It means that f1 can be substituted for f2.
  * NOT[NOT[Int]] <:< OR[Int, String] equiv
  * NOT[Int]=>Nothing <:< (NOT[Int] with NOT[String]) => Nothing equiv
  * Int=>Nothing=>Nothing <:< (Int=>Nothing with String=Nothing) => Nothing
  * */
  implicitly[Function1[Function1[Int, Nothing], Nothing] <:< Function1[Function1[Int, Nothing] with Function1[String, Nothing], Nothing]]
  implicitly[Function1[Human, Progressive] <:< Function1[American, Democrat]]

  implicitly[NOT[NOT[Human]] =:= Function1[Function1[Human, Nothing], Nothing]]

}
