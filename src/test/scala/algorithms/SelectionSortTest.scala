/*
 * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package algorithms

class SelectionSortTest extends org.scalatest.funsuite.AnyFunSuite {
  test("sort a list of many integers including duplicates") {
    assert(SelectionSort(List(9, 10, 1, 3, 18, 7, 1, -1, 0, 0, 11)) === List(-1, 0, 0, 1, 1, 3, 7, 9, 10, 11, 18))
  }

  test("sort a list of many integers, no duplicates") {
    assert(SelectionSort(List(5, 2, 4, 6, 1, 3)) === List(1, 2, 3, 4, 5, 6))
  }

  test("sort a list of two integers, no duplicates") {
    assert(SelectionSort(List(5, 2)) === List(2, 5))
  }

  test("sort a list of one integer") {
    assert(SelectionSort(List(5)) === List(5))
  }

  test("sort an empty list") {
    assert(SelectionSort(List()) === List())
  }

  test("sort a list of string") {
    assert(SelectionSort(List("Mark", "John", "Jeff", "6Pack", "Andrew", "Vic")) === List("6Pack", "Andrew", "Jeff", "John", "Mark", "Vic"))
  }

}
