/*
 * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package algorithms

import scala.annotation.tailrec

/*
  The input is the list of unsorted items. The output is the sorted list.
  In the imperative implementation, the unsorted list is iterated in the outer loop,
  and for each element of the unsorted list in the outer loop there is an inner loop
  where the elements of the unsorted list are iterated from the first up to the first element
  of the outer loop starting backwards from the element before the current one.
  While iterating in the inner loop, if it is determined that the inner loop element is greater
  than the current outer loop element, then these elements are swapped.
 */

class InsertionSort[T](val input: List[T])(implicit ev1: T => Ordered[T]) {
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
        case head :: nil => if (element2Insert <= head) List(element2Insert, head) else List(head, element2Insert)
        case List() => List(element2Insert)
      }
    }

    /*
      This function takes the head element of the list2Sort and inserts it to the
      sorted list using the function addElement2TheCorrectPosition. This function is
      recursive with the base case with an empty list to sort.
     */
    @tailrec def sort(list2Sort: List[T], sortedList: List[T]): List[T] = {
      list2Sort match {
        case head :: tail => sort(tail, addElement2TheCorrectPosition(sortedList, head))
        case List() => sortedList
      }
    }

    /*
    This is the entry point function. It is called recursively to immitate the outer loop
    where the elements of the list are iterated using the head::tail pattern. The second argument
    is the resulting sorted list that is initially empty.
     */
    sort(input, List())
  }


}

object InsertionSort {
  def apply[T](input: List[T])(implicit ev1: T => Ordered[T]): List[T] = new InsertionSort(input).sort
}