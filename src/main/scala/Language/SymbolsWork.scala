
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Language

object SymbolsWork:
  def performAction(action: Symbol): Unit =
    action match
      case Symbol("start") => println("Starting process...")
      case Symbol("stop") => println("Stopping process...")
      case Symbol("pause") => println("Pausing process...")
      case other => println(s"Unknown action: ${other.name}")

  @main def runSymbolsWork(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Language/SymbolsWork.scala created at time 11:50AM")
    performAction(Symbol("start")) // Outputs: Starting process...
    performAction(Symbol("stop")) // Outputs: Stopping process...
    performAction(Symbol("unknown")) // Outputs: Unknown action: unknown

