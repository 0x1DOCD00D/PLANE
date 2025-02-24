////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package ReflectionExperiments

import ReflectionExperiments.OutputPartialFuncSourceCode.pf

object OutputPartialFuncSourceCode:
  val pf: PartialFunction[Any, Unit] = {
    case 1 => println("One")
    case 2 => println("Two")
    case _ => println("Other")
  }

@main def runOutputPartialFuncSourceCode(args: String*): Unit =
  println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/ReflectionExperiments/OutputPartialFuncSourceCode.scala created at time 8:17PM")
  val lines: List[String] = CodeBlockExtractor.codeBlockToStrings {
    val pf: PartialFunction[Any, Unit] = {
      case 1 => println("One")
      case 2 => println("Two")
      case _ => println("Other")
    }
  }
  lines.foreach(println)
