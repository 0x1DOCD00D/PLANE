/*
 * Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package FPIntro

object PersonUnapply:
  class X(val name: String)
  case class Person(name: String, age: Int)

  def getPerson(x: X): Person = {
    Person(x.name, x.name.length * 10)
  }

  object GetPerson {
    def unapply(x: X): Option[Person] = Some(getPerson(x))
  }

  def doTheMatch(x: X) = x match {
    case GetPerson(Person(name, age)) if name == "Alice" => println(s"Alice is $age years old.")
    case GetPerson(Person(name, age)) if name == "Bob"   => println(s"Bob is $age years old.")
    case GetPerson(Person(name, age)) => println(s"Person $name is $age years old.")
    case _                            => println("Unknown person.")
  }

  @main def runPersonUnapply(args: String*): Unit =
    println(
       "File /Users/drmark/IdeaProjects/PLANE/src/main/scala/FPIntro/PersonUnapply.scala created at time 3:04PM"
    )
    doTheMatch(new X("Alice"))
    doTheMatch(new X("Bob"))
    doTheMatch(new X("Charlie"))
