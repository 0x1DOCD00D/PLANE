////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scala3Features

import scala.deriving.Mirror
import scala.compiletime.{constValueTuple, erasedValue, summonInline}

trait JsonEncoder[A] {
  def encode(value: A): String
}

object JsonEncoder {
  given JsonEncoder[String] = value => "\"" + value + "\""

  given JsonEncoder[Int] = value => value.toString

  inline given derived[T](using m: Mirror.ProductOf[T]): JsonEncoder[T] = (value: T) => {
    val elemNames = constValueTuple[m.MirroredElemLabels].toList.asInstanceOf[List[String]]
    val elemInstances = summonAll[m.MirroredElemTypes]
    val elems = value.asInstanceOf[Product].productIterator.toList
    val encodedFields = elemNames.zip(elems.zip(elemInstances)).map {
      case (name, (elem, enc: JsonEncoder[t])) =>
        s""""$name": ${enc.asInstanceOf[JsonEncoder[Any]].encode(elem)}"""
    }
    s"{ ${encodedFields.mkString(", ")} }"
  }

  inline def summonAll[T <: Tuple]: List[JsonEncoder[?]] =
    inline erasedValue[T] match
      case _: EmptyTuple => Nil
      case _: (t *: ts)  => summonInline[JsonEncoder[t]] :: summonAll[ts]
}

case class PersonX(name: String, age: Int) derives JsonEncoder

@main def runJsonEncoder(args: String*): Unit =
  println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/scala3Features/JsonEncoder.scala created at time 10:00AM")
  val p = PersonX("Alice", 25)
  println(summon[JsonEncoder[PersonX]].encode(p))
  case class Address(city: String, zip: Int) derives JsonEncoder
  case class Person(name: String, age: Int, address: Address) derives JsonEncoder

  println(summon[JsonEncoder[Person]].encode(Person("Alice", 25, Address("New York", 10001))))
