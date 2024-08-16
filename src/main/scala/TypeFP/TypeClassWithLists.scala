/*
 *
 *  * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package TypeFP

object TypeClassWithLists {

  /*
  * Our goal is to define a set of list processing utilities that reduce the elements
  * of a list to some value. For example, for a list of integers this reduction may sum or
  * multiply the elements of the list and for a list of booleans it will AND these elements.
  * */
  trait ReduceList[T] {
    def reduce(list: List[T]): T
  }

  /*
  * In the overloaded method, DoSomethingWithListOriginal we should create an object
  * from the trait ReduceList and parameterize it according to the required type. The code
  * is repeated and
  * */
  def DoSomethingWithListOriginal(list: List[Int]): Int = {
    val reduceList = new ReduceList[Int] {
      override def reduce(list: List[Int]): Int = list.sum
    }
    reduceList.reduce(list)
  }

  def DoSomethingWithListOriginal(list: List[Boolean]): Boolean = {
    val reduceList = new ReduceList[Boolean] {
      override def reduce(list: List[Boolean]): Boolean = list.foldRight(true)(_ && _)
    }
    reduceList.reduce(list)
  }

  /*
  * Instead, let's create implicit object that implement
  * the trait's method for specific type parameters. These implicit
  * objects can be used to provide the needed methods of the trait
  * */
  implicit object ReduceIntList extends ReduceList[Int] {
    override def reduce(list: List[Int]): Int = list.sum
  }

  implicit object ReduceBooleanList extends ReduceList[Boolean] {
    override def reduce(list: List[Boolean]): Boolean = list.foldRight(true)(_ && _)
  }

  def DoSomethingWithList[T](list: List[T])(implicit rl: ReduceList[T]): T = {
    rl.reduce(list)
  }

  DoSomethingWithList(List(true, true, false))
}
