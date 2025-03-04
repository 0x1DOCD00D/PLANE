////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scala3Features

//https://users.scala-lang.org/t/retrieve-type-of-a-namedtuple-field/10596/3

object NamedFieldType:

  import scala.NamedTuple.{AnyNamedTuple, Names}
  import scala.NamedTupleDecomposition.DropNames

  import scala.compiletime.error
  import scala.compiletime.ops.int.S
  import scala.compiletime.summonInline

  import scala.reflect.ClassTag

  type IndexOf[T <: Tuple, V <: String] = IndexOf_Recursive[T, V, 0]

  type IndexOf_Recursive[T <: Tuple, V <: String, I <: Int] <: Int = T match {
    case EmptyTuple => Nothing
    case head *: tail =>
      head match {
        case V => I
        case _ => IndexOf_Recursive[tail, V, S[I]]
      }
  }

  type ElemNamed[T <: AnyNamedTuple, NAME <: String] =
    Tuple.Elem[DropNames[T], IndexOf[Names[T], NAME]]

  // Test 1

  case class Person(name: String, age: Int)

  type Fields = NamedTuple.From[Person]
  type AgeField = ElemNamed[Fields, "age"] // works: Int

  // Test 2

  case class Field[T](name: String)(using val classTag: ClassTag[T])

  inline def fieldSelector[T: ClassTag](name: String & Singleton) =
    type Fields = NamedTuple.From[T]
    type FieldTpe = ElemNamed[Fields, name.type]
    val fieldTpe = summonInline[ClassTag[FieldTpe]]
    Field[FieldTpe](name)(using fieldTpe)

  @main def runNamedFieldType(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/scala3Features/NamedFieldType.scala created at time 7:50PM")
    val field = fieldSelector[Person]("name") // no ClassTag available for `FieldTpe`
    println(field)
