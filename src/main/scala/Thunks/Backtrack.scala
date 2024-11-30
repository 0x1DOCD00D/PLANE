
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Thunks

object Backtrack:
  def backtrack[A](choices: List[A], isSolution: A => Boolean, nextChoices: A => List[A]): Option[A] = {
    def attempt(thunks: List[() => Option[A]]): Option[A] = thunks match {
      case Nil => None
      case head :: tail => head() orElse attempt(tail)
    }

    attempt(choices.map { choice =>
      () => {
        if (isSolution(choice))
          println(s"Visiting choice $choice")
          Some(choice)
        else
          println(s"backtracking $choice")
          backtrack(nextChoices(choice), isSolution, nextChoices)
      }
    })
  }

  @main def runBacktrack(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Thunks/Backtrack.scala created at time 3:46PM")
    val choices = List(1, 2, 3)
    val solution = backtrack(choices, (i:Int)=>i == 2 || i == 3, _ => List.empty)
    println(solution) // Output: Some(2)
