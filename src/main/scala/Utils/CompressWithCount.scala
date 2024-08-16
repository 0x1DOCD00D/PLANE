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

object CompressWithCount extends App {
  // Object that takes a string of similar consecutive letters (all letters must be same case) and transforms this into a sequence consisting of the count of each consecutive letter followed by that letter. If a letter only appears once, do not apply a count. This should also demonstrate tail recursion. Also write a method that reverses this so that the output of the first method ran with this method will return the original result.
  //
  //      e.g. given AAAAABBBCCC map this to 5A3B3C
  //
  //              if you have some singles do not show "1", so AAAAABCDDD maps to 5ABC3D
  //
  //             the reverse maps the right side to the left.
  def apply(input: String): String = {
    def compressWithCount(inList: List[Char], count: Int, accumulator: List[Char]): List[Char] = {
      inList match {
        case head :: tail => if (tail != Nil && head == tail.head) compressWithCount(tail, count + 1, accumulator) else {
          if (count > 0) {
            compressWithCount(tail, 0, accumulator ::: (count + 1).toString.toList ::: List(head))
          } else {
            compressWithCount(tail, 0, accumulator ::: List(head))
          }
        }
        case Nil => accumulator
      }
    }

    compressWithCount(input.toList, 0, List()).mkString
  }

  println(apply("AAAAABBBCCC"))
  assert(apply("AAAAABBBCCC") == "5A3B3C")
  assert(apply("AAAAABCDDD") == "5ABC3D")
  assert(apply("AAAAABCD") == "5ABCD")
}
