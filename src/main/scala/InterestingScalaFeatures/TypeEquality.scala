////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package InterestingScalaFeatures

//https://users.scala-lang.org/t/checking-for/10736

object TypeEquality:
  def printIfEqual[
     A <: Singleton,
     B <: Singleton
  ](a: A, b: B)(using ev: A =:= B = null): Unit =
    Option(ev) match
      case Some(eq) =>
        println(s"$a == $b")
      case None =>
        println(s"$a != $b")
  end printIfEqual

  @main def runTypeEquality(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/InterestingScalaFeatures/TypeEquality.scala created at time 5:44PM")
    printIfEqual(1, 1)
    printIfEqual(1, 2)
