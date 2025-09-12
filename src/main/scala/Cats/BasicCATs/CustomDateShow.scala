
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.BasicCATs

object CustomDateShow:
  import cats.Show
  import cats.syntax.show.*

  import java.util.Date

  final case class Cat(name: String, age: Int, color: String)

  given Show[Date] with
    def show(t: Date): String = s"${t.toString} (epoch: ${t.getTime})"

  given Show[Cat] with
    def show(c: Cat): String = s"${c.name} is a ${c.age} year-old ${c.color} cat."

  @main def runCustomDateShow(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Cats/CustomDateShow.scala created at time 2:03PM")
    println(new Date().show)
    println(Cat("Garfield", 38, "orange and black").show)
