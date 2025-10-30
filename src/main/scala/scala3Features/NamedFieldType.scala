////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scala3Features

//https://users.scala-lang.org/t/retrieve-type-of-a-namedtuple-field/10596/3

object NamedFieldType:

  import scala.deriving.Mirror
  import scala.compiletime.{erasedValue, summonInline, constValue, error}
  import scala.reflect.ClassTag

  // ----------------------------
  // Demo model
  // ----------------------------
  case class Person(name: String, age: Int)

  // The "result" container; it carries a ClassTag[T]
  case class Field[T](name: String)(using val classTag: ClassTag[T])

  // ----------------------------
  // Type-level utilities on tuples
  // ----------------------------

  /** Field type by name: select the type in `Types` whose label in `Labels` equals N. */
  type SelectType[Labels <: Tuple, Types <: Tuple, N <: String] <: Any = Labels match
    case N *: _      => Tuple.Head[Types]
    case _ *: ls     => SelectType[ls, Tuple.Tail[Types], N]
    case EmptyTuple  => Nothing  // (unreachable if we guard with `RequireHasField`)

  /** Boolean: does `Labels` contain `N`? */
  type Contains[Labels <: Tuple, N <: String] <: Boolean = Labels match
    case N *: _      => true
    case _ *: ls     => Contains[ls, N]
    case EmptyTuple  => false

  /** Emit a compile-time error if `N` is not in `Labels`. */
  inline def RequireHasField[Labels <: Tuple, N <: String](): Unit =
    inline erasedValue[Contains[Labels, N]] match
      case _: true  => ()
      case _: false => error("No such field: " + constValue[N])

  // ----------------------------
  // Selector
  // ----------------------------

  /** Select a field by name (literal) and return a Field[T] where T is the field's type. */
  transparent inline def fieldSelector[T](name: String & Singleton)(using m: Mirror.ProductOf[T])
  : Field[SelectType[m.MirroredElemLabels, m.MirroredElemTypes, name.type]] =
    // Bring the dependent mirror types into local type aliases
    type Labels = m.MirroredElemLabels
    type Types  = m.MirroredElemTypes
    type NAME   = name.type
    // Ensure the field exists – produce a *compile-time* error if not
    RequireHasField[Labels, NAME]()
    // Compute the field type and materialize its ClassTag
    type FieldTpe = SelectType[Labels, Types, NAME]
    val ct = summonInline[ClassTag[FieldTpe]]
    Field[FieldTpe](name)(using ct)

  // ----------------------------
  // Demo
  // ----------------------------

  @main def runNamedFieldType(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/scala3Features/NamedFieldType.scala created at time 7:50PM")
    val field = fieldSelector[Person]("name") // no ClassTag available for `FieldTpe`
    println(field)

    val fName = fieldSelector[Person]("name") // Field[String]
    val fAge = fieldSelector[Person]("age") // Field[Int]
    println(s"name -> ${fName.classTag}") // prints java.lang.String
    println(s"age  -> ${fAge.classTag}") // prints int
    
    // Uncomment to see a *compile-time* error for unknown field:
    // val fBad = fieldSelector[Person]("zzz")
