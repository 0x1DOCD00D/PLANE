/*
 * Copyright (c) 2025-2026 Dr. Mark Grechanik and Lone Star Consulting, Inc.
 *
 * Created or updated on: 2026-06-14 12:28
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 * All rights reserved. This software, source code, documentation, designs, algorithms, analyses, and related materials are the exclusive property of Dr. Mark Grechanik and Lone Star Consulting, Inc. No rights are granted to copy, modify, distribute, sublicense, publish, disclose, reverse engineer, or create derivative works from this material except by prior written authorization from Dr. Mark Grechanik or Lone Star Consulting, Inc.
 */

package ReflectionExperiments

object BlockCodeExecutor:

  import dotty.tools.dotc.Driver
  import java.io.File
  import java.net.URLClassLoader
  import java.nio.file.Files
  import java.lang.reflect.InvocationTargetException

  // Classpath of the running process, so generated code can see
  // LazyRuntimeEval and anything else already on the app classpath.
  private def currentClasspath: String =
    System.getProperty("java.class.path")

  // One full compile + isolated ClassLoader per line, mirroring how a
  // ToolBox compiled each eval independently.
  private def compileAndRun(line: String, idx: Int): Any =
    val className = s"Generated$idx"
    val outDir    = Files.createTempDirectory(s"blockeval$idx").toFile
    val srcFile   = File.createTempFile(className, ".scala")

    val src =
      s"""object $className {
         |  def run(): Any = {
         |    $line
         |  }
         |}
         |""".stripMargin
    Files.write(srcFile.toPath, src.getBytes)

    val report = new Driver().process(Array(
      "-d", outDir.getAbsolutePath,
      "-classpath", currentClasspath,
      srcFile.getAbsolutePath
    ))
    if report.hasErrors then
      throw new RuntimeException(s"Compilation failed for: $line")

    // Per-generation ClassLoader isolation.
    val loader = new URLClassLoader(Array(outDir.toURI.toURL), getClass.getClassLoader)
    val module = loader.loadClass(s"$className$$").getField("MODULE$").get(null)
    try module.getClass.getMethod("run").invoke(module)
    catch case e: InvocationTargetException => throw e.getCause

  def evaluateBlock[T](code: List[String]): List[T] =
    code.zipWithIndex.map { case (line, idx) =>
      try
        println(s"Executing line: $line")
        compileAndRun(line, idx).asInstanceOf[T]
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