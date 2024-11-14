
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package FPIntro

//https://learning.oreilly.com/library/view/functional-programming-in/9781617299582/OEBPS/Text/04.htm#heading_id_3
object ExceptionsAreNotReferentiallyTransparent:
  def failingFn(i: Int): Int =
//    val y: Int = throw Exception("fail!")
    try
//      i + y
      i + ((throw Exception("fail!")): Int)
    catch
      case e: Exception => 2*i

  @main def runExceptionsAreNotReferentiallyTransparent(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/FPIntro/ExceptionsAreNotReferentiallyTransparent.scala created at time 7:02PM")
    println(failingFn(5))
