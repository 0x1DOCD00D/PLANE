////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scala3Features

import scala.deriving.Mirror
import scala.compiletime.{constValueTuple, erasedValue, summonInline}

trait PrettyPrinter[A] {
  def pretty(value: A): String
}

object PrettyPrinter {
  given PrettyPrinter[String] = value => s""""$value""""
  given PrettyPrinter[Int] = _.toString

  inline given derived[T](using m: Mirror.ProductOf[T]): PrettyPrinter[T] = (value: T) => {
    val elemNames = constValueTuple[m.MirroredElemLabels].toList.asInstanceOf[List[String]]
    val elemInstances = summonAll[m.MirroredElemTypes]
    val elems = value.asInstanceOf[Product].productIterator.toList
    val fields = elemNames.zip(elems.zip(elemInstances)).map {
      case (name, (elem, enc: PrettyPrinter[t])) =>
        s"$name=${enc.asInstanceOf[PrettyPrinter[Any]].pretty(elem)}"
    }
    s"(${fields.mkString(", ")})"
  }

  inline def summonAll[T <: Tuple]: List[PrettyPrinter[?]] =
    inline erasedValue[T] match
      case _: EmptyTuple => Nil
      case _: (t *: ts)  => summonInline[PrettyPrinter[t]] :: summonAll[ts]
}

case class Car(brand: String, year: Int) derives PrettyPrinter

@main def runPrettyPrinter(args: String*): Unit =
  println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/scala3Features/PrettyPrinter.scala created at time 10:38AM")
  val car = Car("Tesla", 2025)
  println(summon[PrettyPrinter[Car]].pretty(car)) // Car(brand="Tesla", year=2022)
