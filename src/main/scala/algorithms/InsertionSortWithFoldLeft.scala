/*
 * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package algorithms

class InsertionSortWithFoldLeft[T](val input: List[T])(implicit ev1: T => Ordered[T]) {
  /*
    In the functional solution, we break the unsorted list using the head::tail pattern. Also, we introduce
    a sorted list that we also decompose using the same pattern to insert a new element from the unsorted list
    into the right position in the newly formed sorted list.
   */

  def sort: List[T] = {
    def addElement2TheCorrectPosition(sortedList: List[T], element2Insert: T): List[T] = {
      sortedList match {
        case head :: tail => if (element2Insert <= head) element2Insert :: sortedList else {
          head :: addElement2TheCorrectPosition(tail, element2Insert)
        }
        case List() => List(element2Insert)
      }
    }

    input.foldLeft(List[T]())((sortedList, element) => addElement2TheCorrectPosition(sortedList, element))
  }
}

object InsertionSortWithFoldLeft {
  def apply[T](input: List[T])(implicit ev1: T => Ordered[T]): List[T] = new InsertionSortWithFoldLeft(input).sort
}