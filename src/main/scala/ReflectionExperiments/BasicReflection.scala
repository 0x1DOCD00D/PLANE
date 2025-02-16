////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package ReflectionExperiments

import scala.reflect.runtime.universe

object BasicReflection:

  import scala.quoted.*

  given staging.Compiler = staging.Compiler.make(getClass.getClassLoader)

  staging.run('{ println("Hello, staging compiler!") })

  import scala.tools.reflect.ToolBox

  val tb: ToolBox[universe.type] = scala.reflect.runtime.universe
    .runtimeMirror(getClass.getClassLoader)
    .mkToolBox()
  tb.eval(tb.parse("""println("Hello, scala 2 reflection!")"""))
  @main def runBasicReflection(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/ReflectionExperiments/BasicReflection.scala created at time 12:44 PM")
