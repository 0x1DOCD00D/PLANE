////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scala3Features

// https://users.scala-lang.org/t/generic-tuple-to-case-class-conversion-methods/10677/2

import scala.deriving.Mirror
import scala.compiletime.{erasedValue, summonInline}

object Tuples2CaseClass:
  case class Person(name: String, age: Int)
  inline def tupleToCaseClass[T](tuple: Tuple)(using m: Mirror.ProductOf[T]): T =
    m.fromProduct(tuple)

  @main def runTuples2CaseClass(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/scala3Features/Tuples2CaseClass.scala created at time 7:24AM")
    val data = ("Alice", 30)
    val person = tupleToCaseClass[Person](data)
    println(person)
