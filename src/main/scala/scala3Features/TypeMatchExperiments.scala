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

  type SomeMatchingType[T] = T match
    case None.type   => None.type
    case Some[p]     => SomeOtherType[p]
    case String      => Option[Int]
    case Array[p]    => SomeMatchingType[Option[p]]
    case Iterable[p] => SomeMatchingType[Option[p]]
    case p *: _      => SomeMatchingType[Option[p]]
    case Any         => List[Nothing]

  def produceSomeOtherTypeValue[Q](p: Q): SomeOtherType[Q] = new SomeOtherType[Q] { val value = p }

  @tailrec def behavior[TypeVar](parm: TypeVar): SomeMatchingType[TypeVar] = parm match
    case x: None.type   => None
    case x: Some[?]     => produceSomeOtherTypeValue(x.get)
    case x: String      => Some(x.length)
    case x: Array[_]    => behavior(x.headOption)
    case x: Iterable[_] => behavior(x.headOption)
    case x: (_ *: _)    => behavior(Option(x.head))
    case x: Any         => List[Nothing]()

  @main def runTypeMatchExperiments(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/scala3Features/TypeMatchExperiments.scala created at time 9:56AM")
    println(behavior(None))
    println(behavior(Some(1)).asInstanceOf[SomeOtherType[Int]].value)
    println(behavior("Hello").get.asInstanceOf[Int])
    println(behavior(Array(10, 2, 3)).asInstanceOf[SomeOtherType[Int]].value)
    println(behavior(List("100", 2, 3)).asInstanceOf[SomeOtherType[String]].value)
    println(behavior(1 *: 2 *: 3 *: EmptyTuple).asInstanceOf[SomeOtherType[Int]].value)
    println(behavior(1))
