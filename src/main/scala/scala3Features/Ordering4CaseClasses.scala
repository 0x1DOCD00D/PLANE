
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scala3Features

import scala.deriving.Mirror
import scala.compiletime.{erasedValue, summonInline}

trait Ordering4CaseClasses[A] {
  def compare(x: A, y: A): Int
}

object Ordering4CaseClasses {
  given Ordering4CaseClasses[Int] = _ compareTo _
  given Ordering4CaseClasses[String] = _ compareTo _

  inline given derived[T](using m: Mirror.ProductOf[T]): Ordering4CaseClasses[T] = (x: T, y: T) => {
    val elemInstances = summonAll[m.MirroredElemTypes]
    val xElems = x.asInstanceOf[Product].productIterator.toList
    val yElems = y.asInstanceOf[Product].productIterator.toList
    val comparisons = xElems.zip(yElems).zip(elemInstances).map {
      case ((xe, ye), ord: Ordering4CaseClasses[t]) =>
        ord.asInstanceOf[Ordering4CaseClasses[Any]].compare(xe, ye)
    }
    comparisons.find(_ != 0).getOrElse(0)
  }

  inline def summonAll[T <: Tuple]: List[Ordering4CaseClasses[?]] =
    inline erasedValue[T] match
      case _: EmptyTuple => Nil
      case _: (t *: ts) => summonInline[Ordering4CaseClasses[t]] :: summonAll[ts]
}

case class Student(name: String, age: Int) derives Ordering4CaseClasses

@main def runOrdering4CaseClasses(args: String*): Unit =
  println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/scala3Features/Ordering4CaseClasses.scala created at time 10:42AM")
  val s1 = Student("Alice", 25)
  val s2 = Student("Bob", 22)
  println(summon[Ordering4CaseClasses[Student]].compare(s1, s2)) // positive value because "Alice" > "Bob"
