////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scala3Features

object TypeClassInduction:
  // Step 1: Define the Type Class
  trait TypeName[A]:
    def method(a: A): String

  // Step 2: Base Case - Define `TypeName` instances for primitive types
  given TypeName[Int] with
    def method(a: Int): String = a.toString

  given TypeName[String] with
    def method(a: String): String = s""""$a""""

  // Step 3: Inductive Case - Define `TypeName` for Lists
  given [A](using sa: TypeName[A]): TypeName[List[A]] with
    def method(lst: List[A]): String =
      lst.map(sa.method).mkString("[", ", ", "]")

  // Step 4: Inductive Case - Define `TypeName` for Options
  given [A](using sa: TypeName[A]): TypeName[Option[A]] with
    def method(opt: Option[A]): String =
      opt match
        case Some(value) => s"Some(${sa.method(value)})"
        case None        => "None"

  // Step 5: Helper Function to Display Values
  def someActionOnInputParam[A](a: A)(using s: TypeName[A]): String =
    s.method(a)

  @main def runTypeClassInduction(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/scala3Features/TypeClassInduction.scala created at time 3:21PM")
    println(someActionOnInputParam(58))
    println(someActionOnInputParam("Scala"))
    println(someActionOnInputParam(List(1, 2, 3)))
    println(someActionOnInputParam(List("Scala", "FP")))
    println(someActionOnInputParam(Option("Mark")))
