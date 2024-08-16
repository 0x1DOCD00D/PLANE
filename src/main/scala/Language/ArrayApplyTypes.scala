/*
 * Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Language

import scala.quoted.{Expr, FromExpr, Quotes, Type, Varargs}

object ArrayApplyTypes:
  def _arraylen(xs: Expr[Array[?]])(using Quotes): Expr[Int] =
    xs match
      // case '{ Array(${_}: A, (${Varargs(elems)}: Seq[A])*) } => Expr(elems.length + 1)
      case '{ Array(${ _ }: Int, (${ Varargs(elems) }: Seq[Int])*) } => Expr(elems.length + 1)
      case '{ Array[a]((${ Varargs(elems) }: Seq[a])*)($_) }         => Expr(elems.length)
      case _                                                         => Expr(-1)

  @main def runArrayApplyTypes(args: String*): Unit =
//    println(_arraylen('{Array(1, 2, 3, 4, 5)}))
    println(
       "File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Language/ArrayApplyTypes.scala created at time 9:58AM"
    )
