
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.SemigroupsMonoids

object SemigroupK_v1 {
  def main(args: Array[String]): Unit = {
    import cats.SemigroupK
    import cats.instances.list.*
    import cats.instances.option.*   // for SemigroupK

    val opt1: Option[Int] = Some(1)
    val opt2: Option[Int] = Some(2)
    val optNone: Option[Int] = None

    val optionSemigroupK = SemigroupK[Option]

    // Combine two Some values
    val combinedSome: Option[Int] = optionSemigroupK.combineK(opt1, opt2)
    println(s"Combined Some(1) and Some(2): $combinedSome") // Output: Some(1)

    // Combine Some and None
    val combinedWithNone1: Option[Int] = optionSemigroupK.combineK(opt1, optNone)
    println(s"Combined Some(1) and None: $combinedWithNone1") // Output: Some(1)

    val combinedWithNone2: Option[Int] = optionSemigroupK.combineK(optNone, opt2)
    println(s"Combined None and Some(2): $combinedWithNone2") // Output: Some(2)

    // Combine two None values
    val combinedNone: Option[Int] = optionSemigroupK.combineK(optNone, optNone)
    println(s"Combined None and None: $combinedNone") // Output: None

    // Now for List
    val list1: List[Int] = List(1, 2)
    val list2: List[Int] = List(3, 4)

    val listSemigroupK = SemigroupK[List]

    // Combine two lists
    val combinedList: List[Int] = listSemigroupK.combineK(list1, list2)
    println(s"Combined List(1, 2) and List(3, 4): $combinedList") // Output: List(1, 2, 3, 4)
  }
}
