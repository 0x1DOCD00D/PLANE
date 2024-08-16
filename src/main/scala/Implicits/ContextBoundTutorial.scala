/*
 *
 *  * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package Implicits

object ContextBoundTutorial extends App {

  /*
  * https://docs.scala-lang.org/tutorials/FAQ/context-bounds.html
  * A context bound requires a parameterized type, such as Ordered[A].
  * A context bound describes an implicit value. It is used to declare that for some type A, there is an implicit value of type B[A] available. The syntax goes like this:
  * def f[A : B](a: A) = g(a) // where g requires an implicit value of type B[A]
  * The common example of usage in Scala is this:
  * def f[A : ClassTag](n: Int) = new Array[A](n)
  * An Array initialization on a parameterized type requires a ClassTag to be available, for arcane reasons related to type erasure and the non-erasure nature of arrays.
  * Another very common example in the library is a bit more complex:
  * def f[A : Ordering](a: A, b: A) = implicitly[Ordering[A]].compare(a, b)
  * Here, implicitly is used to retrieve the implicit value we want, one of type Ordering[A], which class defines the method compare(a: A, b: A): Int.
  * */
  case class Safespace[T](person: T)

  trait Woke[T] {
    def triggered(person1: T, person2: T): Option[Safespace[T]]
  }

  case class Human(snowflake: Boolean)

  def scream[T](a: T, b: T)(trigger: Woke[T]): Option[Safespace[T]] = {
    trigger.triggered(a, b)
  }

  val wokeness = new Woke[Human] {
    override def triggered(p1: Human, p2: Human): Option[Safespace[Human]] = if ((p1.snowflake || p2.snowflake) && (!p1.snowflake || !p2.snowflake)) {
      if (p1.snowflake) Some(Safespace(p1))
      else Some(Safespace(p2))
    }
    else None
  }
  println(scream(Human(true), Human(false))(wokeness))
  println(scream(Human(false), Human(true))(wokeness))
  println(scream(Human(true), Human(true))(wokeness))
  println(scream(Human(false), Human(false))(wokeness))

  //  As usual, let's make the mandatory Woke parameter implicit
  given Woke[Human] with {
    override def triggered(p1: Human, p2: Human): Option[Safespace[Human]] = if ((p1.snowflake || p2.snowflake) && (!p1.snowflake || !p2.snowflake)) {
      if (p1.snowflake) Some(Safespace(p1))
      else Some(Safespace(p2))
    }
    else None
  }

  def screamImplicit[T](a: T, b: T)(implicit trigger: Woke[T]): Option[Safespace[T]] = {
    trigger.triggered(a, b)
  }

  println(screamImplicit(Human(true), Human(false)))
  println(screamImplicit(Human(false), Human(true)))
  println(screamImplicit(Human(true), Human(true)))
  println(screamImplicit(Human(false), Human(false)))

  // The context bound is written as T : Woke. It requires the existence of an implicit value for Woke[T]

  def screamImplicitContextBound[T: Woke](a: T, b: T): Option[Safespace[T]] = {
    implicitly[Woke[T]].triggered(a, b)
  }

  println(screamImplicitContextBound(Human(true), Human(false)))
}
