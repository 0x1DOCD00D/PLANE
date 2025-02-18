////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scala3Features

object TypeClassWithDerives:
  import scala.deriving.Mirror
  import scala.compiletime.{constValue, constValueTuple, erasedValue, summonInline}

  trait TypeName[A]:
    def method(a: A): String

  object TypeName:
    given TypeName[String] with
      def method(a: String): String = s"String: $a"

    given TypeName[Int] with
      def method(a: Int): String = s"Int: ${a.toString}"

    inline given derived[T](using m: Mirror.ProductOf[T]): TypeName[T] =
      new TypeName[T]:
        def method(a: T): String =
          val className = constValue[m.MirroredLabel] // Extracts case class name
          val elemNames = constValueTuple[m.MirroredElemLabels].toList.asInstanceOf[List[String]]
          val elemInstances = summonAll1[m.MirroredElemTypes]
          val elems = a.asInstanceOf[Product].productIterator.toList
          val formattedFields = elemNames.zip(elems.zip(elemInstances)).map:
            case (name, (elem, enc: TypeName[t])) =>
              s"$name=${enc.asInstanceOf[TypeName[Any]].method(elem)}"
          s"$className(${formattedFields.mkString(", ")})"

    inline def summonAll1[T <: Tuple]: List[TypeName[?]] =
      inline erasedValue[T] match
        case _: EmptyTuple => Nil
        case _: (t *: ts)  => summonInline[TypeName[t]] :: summonAll1[ts]

  case class Person(name: String, age: Int, address: Address) derives TypeName
  case class Address(country: String, zip: Int) derives TypeName

  def someActionOnInputParam[A](a: A)(using s: TypeName[A]): String = s.method(a)

  @main def runTypeClassWithDerives(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/scala3Features/TypeClassWithDerives.scala created at time 12:48PM")
    val p = Person("Mark", 58, Address("USA", 12345))
    println(someActionOnInputParam(p))
