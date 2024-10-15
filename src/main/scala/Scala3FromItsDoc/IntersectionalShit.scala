////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Scala3FromItsDoc

object NewTypes:
  object Intersex {
    trait Resettable:
      def reset(): Unit

    trait Growable[T]:
      def add(t: T): Unit

    def f(x: Resettable & Growable[String]): Unit =
      x.reset()
      x.add("grow it!")
  }

  object CommonMembers {
    trait A:
      def children: List[A]

    trait B:
      def children: List[B]

    class C extends A with B {
      override def children: List[A] & List[B] = // or List[A&B]
        println("List of children")
        List(new C)
    }
    val x: A & B = new C
    // A & B is just a type that represents a set of requirements for values of the type
    val ys: List[A & B] = x.children
  }

  @main def runNewTypes(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Scala3FromItsDoc/IntersectionalShit.scala created at time 10:29AM")
    println {
      class X extends Intersex.Resettable with Intersex.Growable[String] {
        override def reset(): Unit = println("reset")

        override def add(t: String): Unit = println(t)
      }
      Intersex.f(new X)
    }
