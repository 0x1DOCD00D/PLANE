/*
 *
 *  * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package Utils

import scala.annotation.tailrec

object ReverseList extends App {
  //Object that takes a generic list and can reverse it using a method that allows tail recursion. The test can implement an instance of any concrete object
  def apply[T](input: List[T]): List[T] = {
    @tailrec def reverseList(list: List[T], accum: List[T]): List[T] = {
      list match {
        case Nil => accum
        case head :: tail => reverseList(tail, head :: accum)
      }
    }

    reverseList(input, List())
  }

  println(apply(List("a1", "b2", "c3")))
}
