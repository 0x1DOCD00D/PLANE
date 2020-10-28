/*
 *
 *  * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package Language

//https://docs.scala-lang.org/tutorials/FAQ/initialization-order.html

object MemberInit extends App {

  abstract class A {
    val member1: String
    val member2: String = "member2"

    println("A: " + member1 + ", " + member2)
  }

  class B extends A {
    val member1: String = "member1"

    println("B: " + member1 + ", " + member2)
  }

  class C extends B {
    override val member2: String = "new value for member 2"

    println("C: " + member1 + ", " + member2)
  }

  new C

  abstract class A1 {
    val member1: String
    val member2: String = "member2"

    println("A1: " + member1 + ", " + member2)
  }


  /* early initializers are deprecated; they will be replaced by trait parameters in 2.14, see the migration guide on avoiding var/val in traits.
    class B1 extends {
      val member1: String = "new member1 value"
    } with A1 {
      println("B1: " + member1 + ", " + member2)
    }

    class C1 extends {
      override val member2: String = "new member2 value"
    } with B1 {
      println("C1: " + member1 + ", " + member2)
    }

    new C1
  */
}
