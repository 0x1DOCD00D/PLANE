////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package ReflectionExperiments

object BlockCodeExecutor:

  import scala.reflect.runtime.universe._
  import scala.tools.reflect.ToolBox

  def evaluateBlock[T](code: List[String]): List[T] =
    val mirror = runtimeMirror(getClass.getClassLoader)
    val toolbox = mirror.mkToolBox()

    code.map { line =>
      try
        println(s"Executing line: $line")
        val tree = toolbox.parse(line)
        toolbox.eval(tree).asInstanceOf[T]
      catch
        case ex: Throwable =>
          println(s"Exception occurred: ${ex.getMessage}")
          ex.toString.asInstanceOf[T]
    }

  @main def runBlockCodeExecutor(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/ReflectionExperiments/BlockCodeExecutor.scala created at time 3:35PM")
    println("Executing dynamically compiled block...")
    val lines: List[String] = CodeBlockExtractor.codeBlockToStrings {
      println("Hello from the block of code!")
      val x = 10
      println(s"x = $x")
      LazyRuntimeEval.z = 100

      throw new RuntimeException("Oops!") // Should NOT stop subsequent statements

      println("We still continue after ignoring the exception!")
      val y = x * 2
      val m = y.asInstanceOf[String]
      println("We keep going after ignoring the second exception!")
      println(s"y = $y")
      LazyRuntimeEval.z
    }.flatMap(_.split("[;\n]")).map(_.trim).filter(_.nonEmpty).dropRight(1).drop(1).dropRight(1)
    println(s"Resulting ${lines.length} lines of code:")
    lines.dropRight(1).foreach(println)

    val computedValue = BlockCodeExecutor.evaluateBlock(lines)

    computedValue.foreach(println)
    println(s"z = ${LazyRuntimeEval.z}")
