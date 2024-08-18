////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Language.CollectionsGame

import scala.io.StdIn.readLine
import scala.util.control.Breaks

object Input2Output:
  val mybreaks = new Breaks
  import mybreaks.{break, breakable}

  @main def runInput2Output(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Language/CollectionsGame/Input2Output.scala created at time 8:45PM")
    breakable {
    collection.Iterator
      .continually(readLine())
      .foreach { line =>
        println(s"got: $line")
        if line == "exit now!" then break
      }
    }
