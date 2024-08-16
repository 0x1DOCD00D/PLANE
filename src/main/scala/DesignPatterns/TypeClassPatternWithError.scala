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

//a type parameter with a context bound is of the form [T: Bound].
// It is expanded to plain type parameter T together with an implicit parameter of type Bound[T].
object TypeClassPatternWithError extends App {

  trait Storage[T] {
    def method1(v: T): T

    def method2(v1: T, v2: T): T
  }

  //def implicitly[T](implicit e: T): T = e
  def SomeMethod[T: Storage](xlst: List[T]): T = implicitly[Storage[T]].method1(xlst.reduce(implicitly[Storage[T]].method2(_, _)))

  //Error:(28, 13) could not find implicit value for evidence parameter of type DesignPatterns.TypeClassPattern.Storage[Int]
  //SomeMethod(List(1,3))
}
