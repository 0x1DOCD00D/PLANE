////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Language

object ContinuationsClassicShiftReset:
  def reset[R](block: => R): R = block

  // Define 'shift' function
  def shift[A, R](f: (A => R) => R): (A => R) => R = k => f(k)

  @main def runContinuationsClassicShiftReset(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Language/ContinuationsClassicShiftReset.scala created at time 11:33AM")
    val result = reset[Int] { 
      val a = 10
      val result = shift { (k: Int => Int) =>
        // Inside shift: capture the continuation 'k'
        println(s"Captured continuation with a = $a")
        // Modify the flow by calling the continuation with a new value
        k(a * 2)
      } { b => b + 5 }
      result
    }
    println(s"Result: $result") // Should print: Result: 25
