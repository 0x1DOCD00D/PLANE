/*
 *
 *  * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package Implicits

object ContextBounds extends App {

  //define an implicit conversion from Int to String and prepend with some string
  implicit class Convert(val v: Int) extends AnyVal {
    def <~(prepend: String): String = prepend ++ v.toString
  }

  //the implicit value is created in this scope
  implicit val v: String = 23 <~ "Converted value: "

  //implicitly[T](implicit e: T): T = e
  //implicitly[String](implicit e: String is located, it is v): T = v is returned
  println(implicitly[String])

  trait MapsTo[RANGE] {
    type From[Domain] = Domain => RANGE

    def method[Input](f: From[Input])(v: Input) = f(v)
  }

  def doTheMapping[Domain: MapsTo[String]#From](t: Domain) = new MapsTo[String] {}.method((a: Domain) => a.toString)(t)

  //Error:(33, 24) No implicit view available from Int => String.
  //val rv = doTheMapping(2)

  def walkList[T](lst: List[T])(implicit s: scala.reflect.ClassTag[T]) = {
    val arr = new Array[T](lst.length)
    //    cannot find class tag for element type T
    //    val arr = new Array[T](lst.length)

    for (iter <- 0 to lst.length) arr(iter) = lst(iter)
  }

  walkList(List("hello ") ++ List(53))

}
