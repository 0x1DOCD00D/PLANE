/*
 *
 *  * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package DesignPatterns

object TypeClass4POJO extends App {

  trait Verify[T] {
    def runChecks(t: T): Boolean
  }

  object Verify {
    implicit def IntVerify: Verify[Int] = new Verify[Int] {
      def runChecks(i: Int) = i > 0
    }

    implicit def StringVerify: Verify[String] = new Verify[String] {
      def runChecks(s: String) = !s.isEmpty
    }
  }

  case class Person(name: String, age: Int)

  object Person {
    implicit def VerifyPerson(implicit verInt: Verify[Int], verString: Verify[String]): Verify[Person] = new Verify[Person] {
      def runChecks(p: Person) = verString.runChecks(p.name) && verInt.runChecks(p.age)
    }
  }

  val p = Person("drmark", 18)
  println(implicitly[Verify[Person]].runChecks(p))
}
