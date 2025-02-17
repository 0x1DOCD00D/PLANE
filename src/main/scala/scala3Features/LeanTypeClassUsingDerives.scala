////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scala3Features

object LeanTypeClassUsingDerives:

  import scala.deriving.Mirror
  import TypeClassInduction.*

  inline given derived[A](using m: Mirror.Of[A]): TypeName[A] = new TypeName[A]:
    def method(a: A): String = s"Derived(${a.toString})"

  case class User(name: String, age: Int)

  @main def runLeanTypeClassUsingDerives(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/scala3Features/LeanTypeClassUsingDerives.scala created at time 3:30PM")
    println(someActionOnInputParam(List(User("Alice", 25), User("Mark", 58))))
