/*
 * Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package FPIntro

object FoldingStuff:
  @annotation.tailrec
  def myFoldLeft[A, B](l: List[A], acc: B)(f: (B, A) => B): B = l match
    case List() => acc
    case h::t => myFoldLeft(t, f(acc, h))(f)

  def foldRightUsingFoldLeft[A, B](l: List[A], acc: B, f: (B, A) => B): B = myFoldLeft(l.reverse, acc)(f)

  @main def runFolding(): Unit =
    val lst = List(1, 2, 3, 4, 5)
    val res = lst.foldLeft(true) {
      (acc, elem) =>
        if elem % 8 == 0 then
          false
        else
          acc
    }
    println(res)

    val lstWords = List("Hello", "World", "Scala", "Programming")
    val res1 = myFoldLeft(lstWords, 0) {
      (acc, elem) =>
        acc + elem.length
    }
    println(res1)
    val res2 = foldRightUsingFoldLeft(lstWords, "", (acc: String, elem: String) => acc + elem)
    println(res2)