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

//we define a companion class
class PimpMyLibrary {
  type Sometype = DesignPatterns.PimpMyLibrary.Student
}

object PimpMyLibrary extends App {

  //https://docs.scala-lang.org/sips/value-classes.html

  implicit class SomeClass(val o: PimpMyLibrary#Sometype) extends AnyVal {
    def SomeMethod = println(s"Called for $o")
  }

  class Student

  val student = new Student

  student.SomeMethod

}
