////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package ReflectionExperiments

import java.io.File
import scala.annotation.experimental

object EvalBlockOfCode:
  @main def runEvalBlockOfCode(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/ReflectionExperiments/EvalBlockOfCode.scala created at time 2:30PM")

    import java.lang.reflect.Field
    val field: Field = LazyRuntimeEval.getClass.getDeclaredField("z")
    field.setAccessible(true)
    field.set(null, 50)
    println(s"Updated LazyRuntimeEval.z = ${field.get(null)}")

    val lines: List[String] = CodeBlockExtractor.codeBlockToStrings {
      println("Hello from the block of code!")
      val x = 10
      println(s"x = $x")

      throw new RuntimeException("Oops!") // Should NOT stop subsequent statements

      println("We still continue after ignoring the exception!")
      val y = x * 2
      val m = y.asInstanceOf[String]
      println("We keep going after ignoring the second exception!")
      println(s"y = $y")
    }
    println("Resulting lines:")
    lines.dropRight(1).foreach(println)

    LazyRuntimeEval.evaluateCode(lines.dropRight(1).mkString(";"))
    println(s"z = ${LazyRuntimeEval.z}")
