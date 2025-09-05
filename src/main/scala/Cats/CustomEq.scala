
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats

object CustomEq:
  import cats.Eq
  import cats.syntax.eq.*

  final case class Cat(name: String, age: Int, color: String)
  final case class Dog(name: String, age: Int, color: String)
  given Eq[Cat] = Eq.instance[Cat] { (cat1, cat2) =>
    (cat1.name === cat2.name) && (cat1.age === cat2.age)
  }
  given Eq[Dog] = Eq.instance[Dog] { (dog1, dog2) =>
    (dog1.name === dog2.name) && (dog1.age === dog2.age)
  }

  @main def runCustomEq(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Cats/CustomEq.scala created at time 2:13PM")
    val cat1 = Cat("Garfield", 38, "orange and black")
    val cat2 = Cat("Garfield", 38, "orange and black")
    val cat3 = Cat("Heathcliff", 40, "orange and black")
    val dog1 = Dog("Odie", 35, "yellow and brown")
    println(s"cat1 is equal to cat2: ${cat1 === cat2}")
    println(s"cat1 is equal to cat3: ${cat1 === cat3}")
//    println(s"cat1 is equal to dog1: ${cat1 === dog1}") // does not compile


