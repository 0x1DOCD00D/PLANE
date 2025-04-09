
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scala3Features

import scala.deriving.Mirror
import scala.compiletime.{erasedValue, summonInline, constValueTuple, summonAll}
import scala.Tuple.*

object NestedTuples2NestedCaseClasses:
  case class Address(city: String, zip: Int)
  case class Person(name: String, age: Int, address: Address)

  inline def tupleToCaseClass[T](tuple: Tuple)(using m: Mirror.ProductOf[T]): T =
    val converted = convertTupleElems[m.MirroredElemTypes](tuple)
    m.fromProduct(converted)

  inline def convertTupleElems[Elems <: Tuple](tuple: Tuple): Tuple =
    inline erasedValue[Elems] match
      case _: EmptyTuple => EmptyTuple
      case _: (head *: tail) =>
        val headConverted = convertElem[head](tuple.head)
        headConverted *: convertTupleElems[tail](tuple.tail)

  inline def convertElem[T](value: Any): Any =
    inline erasedValue[T] match
      case _: Product =>
        inline summonInline[Mirror.ProductOf[T]] match
          case m => tupleToCaseClass[T](value.asInstanceOf[Tuple])(using m)
      case _ => value

  @main def runNestedTuples2NestedCaseClasses(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/scala3Features/NestedTuples2NestedCaseClasses.scala created at time 7:34AM")
    val raw = ("Alice", 30, ("Wonderland", 12345))
    val person = tupleToCaseClass[Person](raw)
    println(person)
