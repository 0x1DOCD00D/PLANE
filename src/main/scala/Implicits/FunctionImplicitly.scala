/*
 * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Implicits

trait SomeGenericType[T] {
  def behavior(parm: T): T
}

object CannotFindImplicitsForParam {
//  def someMethod[T, A[T]](parm:T):T = implicitly[SomeGenericType[T]].behavior(parm)
}

object ContextBound {
  //with context bound we specify that the upper bound for T is SomeGenericType and there is
  //implicit evidence in resolveMethodRef for the instance of this type
  def someMethod[T: SomeGenericType](parm:T):T = implicitly[SomeGenericType[T]].behavior(parm)

  //def implicitly[T](implicit e: T): T = e
  //that is,

  //someMethod(2)
}


object FunctionImplicitly {
//  implicit val resolveMethodRef: SomeGenericType[Int] = new SomeGenericType[Int] {
//    override def behavior(parm: Int): Int = parm
//  }

  //def someMethod[T <% SomeGenericType[T]](parm:T):T = implicitly[SomeGenericType[T]].behavior(parm)

  //with context bound we specify that the upper bound for T is SomeGenericType and there is
  //implicit evidence in resolveMethodRef for the instance of this type
  def someMethod[T: SomeGenericType](parm:T):T = implicitly[SomeGenericType[T]].behavior(parm)
}
