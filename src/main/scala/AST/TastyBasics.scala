////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package AST

import dotty.tools.dotc.{Compiler, Driver}
import java.nio.file.{Files, Paths}
import java.io.File
import scala.util.Using
import scala.quoted.*
import scala.tasty.inspector.*
import scala.tasty.inspector.TastyInspector

class MyInspector extends Inspector:
  def inspect(using Quotes)(tastys: List[Tasty[quotes.type]]): Unit =
    import quotes.reflect.*
    for tasty <- tastys do
      val tree = tasty.ast
      println("=== TASTy Tree ===")
      println(tree.show(using Printer.TreeStructure))

object TastyBasics:
  def writeSource(code: String, filePath: String): Unit =
    val path = Paths.get(filePath)
    Files.createDirectories(path.getParent)
    Files.write(path, code.getBytes("UTF-8"))

  def compileWithScalaC3(sourceFile: String, outputDir: String): Boolean =
    val driver = new Driver
    val runtimeClasspath = System.getProperty("java.class.path")
    val args = Array(
       "-d",
       outputDir,
       "-classpath",
       runtimeClasspath,
       sourceFile
    )
    val run = driver.process(args)
    !run.hasErrors

  def findTastyFile(className: String, outputDir: String): Option[File] =
    val tastyFile = Paths.get(outputDir, s"${className.stripSuffix(".scala")}.tasty").toFile
    if tastyFile.exists() then Some(tastyFile) else None

  @main def runTastyBasics(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/AST/TastyBasics.scala created at time 3:27PM")
    val code =
      """|package tastyDemo
         |
         |class Greeter:
         |  def greet(name: String): String = s"Hello, $name!"
         |""".stripMargin

    val sourcePath = "/Users/drmark/IdeaProjects/PLANE/src/main/resources/TastyExp/Greeter.scala"
    val outputDir = "/Users/drmark/IdeaProjects/PLANE/src/main/resources/TastyExp/tastyDemo/"
    writeSource(code, sourcePath)
    compileWithScalaC3(sourcePath, outputDir)
    TastyInspector.inspectTastyFiles(
       List(outputDir + "Greeter.tasty")
    )(new MyInspector)
