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

//we introduce a type class whose method printItNow can be invoked on different objects
trait SomeTypeWeDefine[T] {
  def printItNow1(input: T): String
}

object TypeClassesStepByStep extends App {

  //suppose we want to write 27.printItNow to output the value as a string
  //a solution is to convert 27 into an instance of some class that has the method printItNow
  //  implicit class WhateverItsNameIs[T: SomeTypeWeDefine](input: T) {
  //    def printItNow = new SomeTypeWeDefine[T] {
  //      override def printItNow1(input: T): String = input.toString
  //    }.printItNow1(input)
  //  }
  // or we can define it (comment out the previous one)
  //  we introduce an implicit parameter, ev for evidence that there is an instance of the needed type in the scope
  //  that can be used
  implicit class WhateverItsNameIs[T](input: T) {
    def printItNow(implicit ev: SomeTypeWeDefine[T]) = ev.printItNow1(input)
  }

  //  implicit def printIt = new SomeTypeWeDefine[Int] {
  //    override def printItNow1(input: Int): String = input.toString
  //  }
  //  or we can use some other implicit
  implicit val someEvidenceOfInstantiatedType: SomeTypeWeDefine[Int] = new SomeTypeWeDefine[Int] {
    override def printItNow1(input: Int): String = input.toString
  }

  27.printItNow

  //or specify the evidence via implicitly
  27.printItNow(implicitly[SomeTypeWeDefine[Int]])
}
