/*
 *
 *  * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package FPIntro

object MonadPattern extends App {

  //define a case class that stores the attributes of students
  case class Student[T](private val ID: T) {
    def get: T = ID

    def someXform[S](xf: T => Student[S]): Student[S] = xf(ID)
  }

  def createStudent[T](id: T): Student[T] = Student(id)

  val UIC_student = createStudent(123)
  val UC_student = createStudent("Jeff")

  println(s"{println(UIC_student.get} is better that {UC_student.get}")

  println(UIC_student.someXform(s => {
    if (s == 123) Student("Golda") else Student("Manny")
  }).get + " is better that " + UC_student.get)

}
