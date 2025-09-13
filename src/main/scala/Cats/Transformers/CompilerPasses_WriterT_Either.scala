
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.Transformers

import cats.*
import cats.data.*
import cats.implicits.*

object CompilerPasses_WriterT_Either:

  sealed trait CompilerError; 
  case object ParseError extends CompilerError; 
  case object TypeError extends CompilerError

  type Log[A] = WriterT[[X]=>>Either[CompilerError, X], Vector[String], A]
  // Behind the scenes:
  //   WriterT[F, W, A] == F[(W, A)] with Monoid[W].
  //   Here F is Either[CompilerError, *], W is Vector[String] (monoidal via ++), so:
  //   Log[A] == Either[CompilerError, (Vector[String], A)] wrapped in a nice API.

  def log(msg: String): Log[Unit] =
    WriterT.tell(Vector(msg))

  def parse(code: String): Log[List[String]] =
    if code.trim.isEmpty then WriterT.liftF(Left(ParseError))
    else log(s"parsed ${code.length} chars") *> WriterT.value(List("x", "y"))

  def typeCheck(ast: List[String]): Log[List[String]] =
    if ast.contains("bad") then WriterT.liftF(Left(TypeError))
    else log(s"type-checked ${ast.size} nodes") *> WriterT.value(ast)

  def optimize(ast: List[String]): Log[List[String]] =
    log("inliner+const-fold") *> WriterT.value(ast.distinct)

  def pipeline(src: String): Log[List[String]] =
    for
      ast   <- parse(src)
      typed <- typeCheck(ast)
      opt   <- optimize(typed)
      _     <- log(s"ok: ${opt.mkString(",")}")
    yield opt

  @main def runCompiler(): Unit =
    val r1 = pipeline("let x = 1").run
    println(r1) // Right((Vector(parsed 10 chars, type-checked 2 nodes, inliner+const-fold, ok: x,y), List(x, y)))

    val r2 = pipeline("").run
    println(r2) // Left(ParseError) — logs preserved up to the failure point
