/*
 * Copyright (c) 2023 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Streams

object LazyListStuff:
  private val lazylist1: LazyList[Int] = loop(0)
  private var offset = 0
  private def loop(v: Int): LazyList[Int] = v #:: loop(v + 1)

  def next(n: Int): List[Int] =
    val result: List[Int] = lazylist1.slice(offset, offset + n).toList
    offset += n
    result

  @main def runLazyListStuff(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Streams/LazyListStuff.scala created at time 1:48 PM")

    println("lazylist1.take(10).toList = " + next(10))
    println("lazylist1.take(10).toList = " + next(1))
    println("lazylist1.take(10).toList = " + next(5))
    println("lazylist1.take(10).toList = " + next(15))
    println("lazylist1.take(10).toList = " + next(1))

/*
    println("lazylist1.take(10).toList = " + lazylist1.slice(0,10).toList)
    println("lazylist1.take(10).toList = " + lazylist1.slice(10,20).toList)
    */
