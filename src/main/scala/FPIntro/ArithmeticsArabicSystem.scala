/*
 *
 *  * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package FPIntro

object ArithmeticsArabicSystem extends App {
  def succ(i: Int) = i + 1

  def pred(i: Int) = i - 1

  def sum(i: Int, j: Int): Option[Int] = {
    if (i < 0 || j < 0) None
    else if (j == 0) Some(i)
    else sum(succ(i), pred(j))
  }

  println(sum(5, 8).get)

  def sub(i: Int, j: Int): Option[Int] = {
    if (i < 0 || j < 0 || i < j) None
    else if (j == 0) Some(i)
    else sub(pred(i), pred(j))
  }

  println(sub(17, 8).get)

  def mult(i: Int, j: Int): Option[Int] = {
    def multAcc(acc: Int, n: Int): Option[Int] = {
      if (n == 0) Some(acc)
      else multAcc(sum(acc, i).get, pred(n))
    }

    if (i < 0 || j < 0) None
    else multAcc(0, j)
  }

  println(mult(5, 8).get)

  def div(i: Int, j: Int): Option[Tuple2[Int, Int]] = {
    def divResult(result: Int, m: Int, n: Int): Option[Tuple2[Int, Int]] = {
      if (m < n) Some((result, m))
      else divResult(succ(result), sub(m, n).get, n)
    }

    if (i < 0 || j <= 0) None
    else divResult(0, i, j)
  }

  println(div(35, 7).get)
  println(div(35, 6).get)

}
