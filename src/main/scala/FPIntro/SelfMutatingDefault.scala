
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package FPIntro

object SelfMutatingDefault:
  var counter = 0

  def nextCounterValue(default: Int = {
    counter += 1
    counter
  }): Int = default
  
  @main def runSelfMutatingDefault(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/FPIntro/SelfMutatingDefault.scala created at time 11:02AM")
    println(nextCounterValue())
    println(nextCounterValue())
    println(counter)

