/*
 * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package algorithms

import scala.math.Ordering.Implicits.infixOrderingOps

/*
  Implemented the functional programming version of the classic merge sort algorithm. The input is split in half, approximately,
  and then each half is sorted recursively until the base cases are reached. Then the sublists are merged.
  The merge explots the head::tail pattern where the heads are compared and a new merged sorted list is constructed
  by recursively calling the function merge with prepending the head of one of the lists.
 */
class ClassicMergeSort {
  def sort[T](input: List[T])(implicit ev1: Ordering[T]): List[T] = {
    def merge(left: List[T], right: List[T]): List[T] = {
      left match {
        case List() => right
        case lhead :: ltail =>
          right match {
            case List() => left
            case rhead :: rtail => if (lhead <= rhead) lhead :: merge(ltail, right) else rhead :: merge(left, rtail)
          }
      }
    }

    input.length match {
      case 0 => List()
      case 1 => input
      case 2 => if (input.head > input.tail.head) input.reverse else input
      case _ => val (left, right) = input.splitAt(input.length / 2)
        merge(this.sort(left), this.sort(right))
    }
  }
}

object ClassicMergeSort {
  def apply[T](input: List[T])(implicit ev1: T => Ordered[T]): List[T] = (new ClassicMergeSort).sort(input)
}
