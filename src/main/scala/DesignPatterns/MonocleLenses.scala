/*
 * Copyright (c) 2023 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package DesignPatterns

object MonocleLenses:
  case class User(name: String, address: Address)

  case class Address(streetNumber: Int, streetName: String)

  val anna: User = User("Anna", Address(12, "high street"))

  import monocle.syntax.all._

  val anna1: User = anna
    .focus(_.name)
    .replace("Bob")

  println(anna)
  println(anna1)

  val anna2:User = anna
    .focus(_.address.streetNumber)
    .modify(_ + 1)
  println(anna)
  println(anna2)

  @main def runMonocleLenses(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/DesignPatterns/MonocleLenses.scala created at time 10:35 AM")