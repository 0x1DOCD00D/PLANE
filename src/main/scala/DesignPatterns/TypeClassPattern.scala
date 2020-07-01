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

//this type class declares some methods that constitute the behavior of this type
//we use this type in the method SomeMethod of the object TypeClassPattern
trait Storage[T] {
  def method1(v: T): T

  def method2(v1: T, v2: T): T
}

object Storage {

  //in this companion object we provide implicit implementations of the instance of the trait Storage parameterized by some types.
  //when using the method implicitly, it will return the implicit object that is resolved to a given type
  implicit object StorageInt extends Storage[Int] {
    override def method1(v: Int): Int = v * v

    override def method2(v1: Int, v2: Int): Int = v1 + v2
  }

}

object TypeClassPattern extends App {
  //An abbreviated syntax for implicit parameters is called Context Bounds. When a method with a type parameter Tt requires an implicit parameter of type Storage[T]:
  //def method[T](implicit st: Storage[t]) can be rewritten as: def method[T: Storage]
  def SomeMethod[T: Storage](xlst: List[T]): T = implicitly[Storage[T]].method1(xlst.reduce(implicitly[Storage[T]].method2(_, _)))

  println(SomeMethod(List(1, 2, 5)))
}
