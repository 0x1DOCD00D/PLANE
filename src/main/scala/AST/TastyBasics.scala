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
import scala.tasty.inspector.TastyInspector
import scala.quoted.*
import scala.tasty.inspector.*
import scala.reflect.ClassTag

class FullTreeInspector extends Inspector:
  def inspect(using Quotes)(tastys: List[Tasty[quotes.type]]): Unit = {
    import quotes.reflect.*

    println("Starting AST traversal...\n")
    for tasty <- tastys do
      val tree = tasty.ast
      Explorer.traverseTree(tree)(Symbol.spliceOwner)

    object Explorer extends TreeTraverser:
      override def traverseTree(tree: Tree)(owner: Symbol): Unit =
        println(s"Node: ${tree.getClass.getSimpleName}")
        println(s"  Code   : ${tree.show(using Printer.TreeShortCode)}")

        tree match
          case Literal(c) =>
            println(s"  Literal constant: ${c.show}")

          case Ident(name) =>
            println(s"  Identifier: $name")

          case Select(qualifier, name) =>
            println(s"  Select: $name from ${qualifier.show}")

          case Apply(fun, args) =>
            println(s"  Apply: function ${fun.show} with arguments:")
            args.foreach(arg => println(s"    - ${arg.show}"))

          case DefDef(name, params, tpt, rhs) =>
            println(s"  DefDef: $name")
            val paramNames = params.flatMap {
              case termClause: TermParamClause => termClause.params.map(_.name)
              case typeClause: TypeParamClause => typeClause.params.map(_.name)
            }
            println(s"    Params: $paramNames")
            println(s"    Return type: ${tpt.tpe.show}")
            println(s"    Body: ${rhs.map(_.show).getOrElse("None")}")

          case ValDef(name, tpt, rhs) =>
            println(s"  ValDef: $name : ${tpt.tpe.show}")
            println(s"    Initializer: ${rhs.map(_.show).getOrElse("None")}")

          case ClassDef(name, _, _, _, _) =>
            println(s"  ClassDef: $name")

          case TypeDef(name, _) =>
            println(s"  TypeDef: $name")

          case Block(stats, expr) =>
            println(s"  Block with ${stats.length} statements and result: ${expr.show}")

          case Inlined(_, _, expr) =>
            println(s"  Inlined expression: ${expr.show}")

          case _ =>
            // Catch other node types not explicitly matched
            println(s"  Tree type: ${tree.getClass.getSimpleName}")

        // Optional: show position info if available
        val pos = tree.pos
        if pos != null then
          println(s"  Position: ${pos.startLine}:${pos.startColumn} - ${pos.endLine}:${pos.endColumn}")
        else
          println("  Position: Not available")
        println() // spacing between nodes
        super.traverseTree(tree)(owner)
    end Explorer
    
  }
end FullTreeInspector

class MyInspector extends Inspector:
  def inspect(using Quotes)(tastys: List[Tasty[quotes.type]]): Unit =
    import quotes.reflect.*
    for tasty <- tastys do
      val tree = tasty.ast
      println("=== TASTy Tree ===")
      println(tree.show(using Printer.TreeStructure))
end MyInspector
      

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

    TastyInspector.inspectTastyFiles(
      List(outputDir + "Greeter.tasty")
    )(new FullTreeInspector)
end TastyBasics
