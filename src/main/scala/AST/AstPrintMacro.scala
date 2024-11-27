////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package AST

import scala.quoted.*

object AstPrintMacro {
  def printASTImpl(expr: Expr[Any])(using Quotes): Expr[Unit] = {
    import quotes.reflect.*
    val tree: Tree = expr.asTerm
    println(tree.show(using Printer.TreeStructure))
    val ast = expr.show
    '{ println(${ Expr(ast) }) }
  }

  def getASTImpl(expr: Expr[Any])(using Quotes): Expr[String] = {
    import quotes.reflect.*
    val tree: Tree = expr.asTerm
    val ts = tree.show(using Printer.TreeStructure)
    Expr(ts)
  }
}
