
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package InterestingScalaFeatures
//https://users.scala-lang.org/t/remove-type-from-union-e-g-omit-in-ts/10768

import scala.annotation.implicitNotFound
import scala.reflect.ClassTag

// ---------------------------------------------------------------------------
//  compile-time evidence that a union U really contains exactly one X
// ---------------------------------------------------------------------------
@implicitNotFound("${U} does not contain ${X}")
sealed trait ContainsWithout[U, X]

/** Low-priority rules live here.  The companion object has higher priority. */
private trait LowPriorityContainsWithout:
  /* fall back to the right arm of the union */
  given right[L, R, X](using ContainsWithout[R, X])
  : ContainsWithout[L | R, X] = new {}

object ContainsWithout extends LowPriorityContainsWithout:
  /* direct hit */
  given base[X]: ContainsWithout[X, X] = new {}

  /* preferred route: look in the left arm first                      */
  /* (higher priority because it is defined in the companion object) */
  given left[L, R, X](using ContainsWithout[L, X])
  : ContainsWithout[L | R, X] = new {}

// ---------------------------------------------------------------------------
//  type-level “set difference”  Remove[U, X]
// ---------------------------------------------------------------------------
type Remove[U, X] = U match
  case X       => Nothing                          // delete the requested member
  case l | r   => Remove[l, X] | Remove[r, X]      // recurse through the union
  case _       => U                                // untouched leaf

// ---------------------------------------------------------------------------
//  heterogeneous container whose type remembers the resident element kinds
// ---------------------------------------------------------------------------
final class ContainerWithout[U](private val elems: Map[Class[?], Any]):

  def get[T](using ClassTag[T]): Option[T] =
    elems.get(summon[ClassTag[T]].runtimeClass).asInstanceOf[Option[T]]

  /** Produce a new container that no longer contains any value of type `X`. */
  inline def without[X](using ClassTag[X], ContainsWithout[U, X])
  : ContainerWithout[Remove[U, X]] =
    new ContainerWithout(elems - summon[ClassTag[X]].runtimeClass)

object ContainerWithout:
  /** Build a container, rejecting duplicate *classes* at run time. */
  def apply[U](values: U*)(using ClassTag[U]): ContainerWithout[U] =
    val m = scala.collection.mutable.Map[Class[?], Any]()
    for v <- values do
      val k = v.getClass
      if m.contains(k) then
        throw new IllegalArgumentException(
          s"duplicate element of type ${k.getSimpleName}"
        )
      m(k) = v
    new ContainerWithout(m.toMap)

// ---------------------------------------------------------------------------
//  demo
// ---------------------------------------------------------------------------
trait Base
case class A(s: String) extends Base
case class B(i: Int)    extends Base
case class C(d: Double) extends Base
case class D()          extends Base          // not present in the container

@main def run(): Unit =
  val c: ContainerWithout[A | B | C] =
    ContainerWithout(A("hi"), B(100), C(3.5))

  val noB  = c.without[B]      // ContainerWithout[A | C]
//  val noC  = noB.without[C]    // ContainerWithout[A]
//  val none = noB.without[A]    // ContainerWithout[Nothing]

  println(noB.get[A])          // Some(A(hi))
//  println(noC.get[B])          // None
//  println(none.get[A])         // None

// c.without[D]              // ⟹  does not compile: A | B | C does not contain D
