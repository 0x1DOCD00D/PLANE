////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scala3Features

import scala.annotation.tailrec

object TypeMatchExperiments:

  trait SomeOtherType[S]:
    val value: S

  // Type-level mapping from input type T to output type
  type SomeMatchingType[T] = T match
    case None.type   => None.type
    case Some[p]     => SomeOtherType[p]
    case String      => Option[Int]
    case Array[p]    => SomeMatchingType[Option[p]]
    case Iterable[p] => SomeMatchingType[Option[p]]
    case (p *: t)    => SomeMatchingType[Option[p]]
    case Any         => List[Nothing]

  def produceSomeOtherTypeValue[Q](p: Q): SomeOtherType[Q] =
    new SomeOtherType[Q] { val value = p }

  // NOTE: tailrec on a generic method sometimes fails; remove @tailrec if the
  // compiler complains. The logic itself is properly tail-recursive.
  @tailrec
  def behavior[A](parm: A): SomeMatchingType[A] =
    parm match
      case _: None.type =>
        None

      case x: Some[t] =>
        // A = Some[t] => SomeMatchingType[A] = SomeOtherType[t]
        produceSomeOtherTypeValue[t](x.get)

      case s: String =>
        // A = String  => SomeMatchingType[A] = Option[Int]
        Some(s.length)

      case arr: Array[t] =>
        // A = Array[t] => SomeMatchingType[A] = SomeMatchingType[Option[t]]
        behavior[Option[t]](arr.headOption)

      case it: Iterable[t] =>
        // A = Iterable[t] => SomeMatchingType[A] = SomeMatchingType[Option[t]]
        behavior[Option[t]](it.headOption)

      case tup: (h *: t) =>
        // Narrow to the exact tuple type (h *: t) so .head has type h (not Tuple.Head[A & (h *: t)])
        val exact: (h *: t) = tup
        val hd: h = exact.head
        behavior[Option[h]](Option(hd))

      case _ =>
        // A = Any fallback => List[Nothing]
        List.empty[Nothing]

  @main def runTypeMatchExperiments(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/scala3Features/TypeMatchExperiments.scala created at time 9:56AM")
    println(behavior(None))
    println(behavior(Some(1)).asInstanceOf[SomeOtherType[Int]].value)
    println(behavior("Hello").get.asInstanceOf[Int])
    println(behavior(Array(10, 2, 3)).asInstanceOf[SomeOtherType[Int]].value)
    println(behavior(List("100", 2, 3)).asInstanceOf[SomeOtherType[String]].value)
    println(behavior(1 *: 2 *: 3 *: EmptyTuple).asInstanceOf[SomeOtherType[Int]].value)
    println(behavior(1))
