////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Language

import scala.quoted.*

trait DefaultValue[T]:
  def value: T

object DefaultValue:
  given DefaultValue[Int] = new DefaultValue[Int] {
    def value = 0
  }

  given DefaultValue[Double] = new DefaultValue[Double] {
    def value = 0.0
  }

  given DefaultValue[String] = new DefaultValue[String] {
    def value = ""
  }

  given DefaultValue[Unit] = new DefaultValue[Unit] {
    def value: Unit = ()
  }

  given [A]: DefaultValue[Option[A]] = new DefaultValue[Option[A]] {
    def value = None
  }

object MacroUtilsWithGenerics:
  import DefaultValue.given
  inline def runWithExceptionHandling[T](inline codeBlock: T): T = ${
    runWithExceptionHandlingImpl[T]('codeBlock)
  }

  def runWithExceptionHandlingImpl[T: Type](codeBlock: Expr[T])(using Quotes): Expr[T] =
    import quotes.reflect.*

    def processStatements(stats: List[Statement]): List[Statement] =
      stats.map {
        case term: Term =>
          val wrapped = '{
            try { ${ term.asExpr } }
            catch { case _: ArithmeticException => () }
          }.asTerm
          wrapped
        case other => other
      }

    def processTerm(term: Term): Term =
      val tExpr = term.asExprOf[T]
      '{
        try { $tExpr }
        catch { case _: ArithmeticException => ${ defaultValue[T] } }
      }.asTerm

    def defaultValue[T: Type]: Expr[T] =
      Expr.summon[DefaultValue[T]] match
        case Some(defaultExpr) => '{ $defaultExpr.value }
        case None              => report.errorAndAbort(s"No given DefaultValue instance found for ${Type.show[T]}")

    codeBlock.asTerm match
      case Inlined(_, _, Block(stats, expr)) =>
        val newStats = processStatements(stats)
        val newExpr = processTerm(expr)
        val block = Block(newStats, newExpr)
        val inlinedBlock = Inlined(None, Nil, block)
        inlinedBlock.asExprOf[T]
      case Inlined(_, _, term) =>
        processTerm(term).asExprOf[T]
      case Block(stats, expr) =>
        val newStats = processStatements(stats)
        val newExpr = processTerm(expr)
        val block = Block(newStats, newExpr)
        val inlinedBlock = Inlined(None, Nil, block)
        inlinedBlock.asExprOf[T]
      case term =>
        processTerm(term).asExprOf[T]
