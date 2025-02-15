////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package ReflectionExperiments

object CodeBlockExtractor:

  import scala.quoted.*
  // Public API: transforms a code block into a List[String] of statement sources
  inline def codeBlockToStrings[T](inline block: => T): List[String] =
    ${ codeBlockToStringsImpl('block) }

  private def codeBlockToStringsImpl(block: Expr[Unit])(using Quotes): Expr[List[String]] =
    import quotes.reflect.*

    // 1) Gather statements from the user's block
    val statements: List[Statement] = block.asTerm match
      case Inlined(_, _, Block(stats, expr)) => stats :+ expr
      case Inlined(_, _, single)             => List(single)
      case b: Block                          => b.statements
      case other                             => List(other)

    // 2) Convert each statement to a "source" string via `.show`
    val stringExprs: List[Expr[String]] =
      statements.map { stmt =>
        Expr(stmt.show) // Convert the AST node to its source-like representation
      }

    // 3) Build a 'List(...)' of strings
    //    We'll splice the individual string expressions into a List literal.
    Expr.ofList(stringExprs).asExprOf[List[String]]

  @main def runCodeBlockExtractor(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/ReflectionExperiments/CodeBlockExtractor.scala created at time 2:05PM")
