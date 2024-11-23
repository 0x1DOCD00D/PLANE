
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Language

import scala.quoted.*

object MacroUtils:
  inline def runWithExceptionHandling(inline codeBlock: Unit): Unit = ${
    runWithExceptionHandlingImpl('codeBlock)
  }

  def runWithExceptionHandlingImpl(codeBlock: Expr[Unit])(using Quotes): Expr[Unit] =
    import quotes.reflect.*

    def processStatements(stats: List[Statement]): List[Statement] =
      stats.map {
        case term: Term =>
          val wrapped = '{
            try { ${ term.asExprOf[Unit] } }
            catch { case _: Exception => }
          }.asTerm
          wrapped
        case other => other
      }

    def processTerm(term: Term): Term =
      '{
        try { ${ term.asExprOf[Unit] } }
        catch { case _: Exception => }
      }.asTerm

    codeBlock.asTerm match
      case Inlined(_, _, Block(stats, expr)) =>
        val newStats = processStatements(stats)
        val newExpr = processTerm(expr)
        Block(newStats, newExpr).asExprOf[Unit]
      case Inlined(_, _, term) =>
        processTerm(term).asExprOf[Unit]
      case Block(stats, expr) =>
        val newStats = processStatements(stats)
        val newExpr = processTerm(expr)
        Block(newStats, newExpr).asExprOf[Unit]
      case term =>
        processTerm(term).asExprOf[Unit]
