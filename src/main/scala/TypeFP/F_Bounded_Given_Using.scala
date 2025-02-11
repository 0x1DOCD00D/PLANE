////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package TypeFP

object F_Bounded_Given_Using:
  trait Fruit[T <: Fruit[T]] {
    def name: String

    def canEqual(other: Any): Boolean = other match
      case _: T => true
      case _    => false

    // Enforce type safety in equals
    final override def equals(obj: Any): Boolean =
      obj match
        case that: T if this.canEqual(that) => this.name == that.name
        case _                              => false
  }

  final case class Apple(name: String) extends Fruit[Apple]
  final case class Orange(name: String) extends Fruit[Orange]

  trait FruitEquality[T <: Fruit[T]]:
    def equals(a: T, b: T): Boolean

  object FruitEquality:
    given appleEquality: FruitEquality[Apple] with
      def equals(a: Apple, b: Apple): Boolean = a.name == b.name

    given orangeEquality: FruitEquality[Orange] with
      def equals(a: Orange, b: Orange): Boolean = a.name == b.name

  def compare[T <: Fruit[T]](a: T, b: T)(using eq: FruitEquality[T]): Boolean = eq.equals(a, b)

  @main def runF_Bounded_Given_Using(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/TypeFP/F_Bounded_Given_Using.scala created at time 1:56PM")
    val apple1 = Apple("Red Apple")
    val apple2 = Apple("Red Apple")
    val apple3 = Apple("Green Apple")

    val orange1 = Orange("Navel Orange")
    val orange2 = Orange("Blood Orange")

    println(compare(apple1, apple2))
    println(compare(apple1, apple3))
    println(compare(orange1, orange2))

// Compilation error! (Cannot compare Apple with Orange)
//   println(compare(apple1, orange1))
