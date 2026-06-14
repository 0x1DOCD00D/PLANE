/*
 * Copyright (c) 2026 Dr. Mark Grechanik and Lone Star Consulting, Inc.
 *
 * Created or updated on: 2026-06-14 12:29
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 * All rights reserved. This software, source code, documentation, designs, algorithms, analyses, and related materials are the exclusive property of Dr. Mark Grechanik and Lone Star Consulting, Inc. No rights are granted to copy, modify, distribute, sublicense, publish, disclose, reverse engineer, or create derivative works from this material except by prior written authorization from Dr. Mark Grechanik or Lone Star Consulting, Inc.
 */


package AST

import dotty.tools.dotc.ast.untpd
import dotty.tools.dotc.config.ScalaSettingCategories.RootSetting
import dotty.tools.dotc.config.Settings.Setting
import dotty.tools.dotc.core.Constants.Constant
import dotty.tools.dotc.core.Contexts.*
import dotty.tools.dotc.parsing.{Parsers, Tokens}
import dotty.tools.dotc.reporting.StoreReporter
import dotty.tools.dotc.util.SourceFile

import javax.script.ScriptEngineManager

object AstFromCodeSnippet {

  /** One initialized compiler context, shared by parsing, traversal and printing.
   *  -usejavacp lets ContextBase.initialize() locate the standard library on the
   *  JVM classpath (run with `fork := true` under sbt so java.class.path is real).
   */
  def makeContext(): Context = {
    val base = new ContextBase
    val ctx0 = base.initialCtx.fresh
    ctx0.setSetting(ctx0.settings.Yusejavacp, true)
    ctx0.setReporter(new StoreReporter())
    base.initialize()(using ctx0)
    ctx0
  }

  /** Parse a sequence of statements (the snippet is not a full compilation unit,
   *  so `x + y` at top level would be rejected by Parser.parse()).
   *
   *  Note for 3.8.3: do NOT use Parsers.parser(source) here. It routes
   *  self-contained sources (every SourceFile.virtual) to ScriptParser, whose
   *  parse() throws `unsupported("parse")`. blockStatSeq(outermost = true) is
   *  exactly what dotty.tools.repl.ParseResult uses.
   */
  def getASTFromCode(code: String)(using Context): untpd.Tree = {
    val source = SourceFile.virtual("Snippet.scala", code)
    val reporter = new StoreReporter()
    val parseCtx = ctx.fresh.setReporter(reporter).setSource(source)

    inContext(parseCtx) {
      val parser = new Parsers.Parser(source)
      val stats = parser.blockStatSeq(outermost = true)
      parser.accept(Tokens.EOF)

      val messages = reporter.removeBufferedMessages
      if messages.nonEmpty then
        throw new IllegalArgumentException(
          messages.map(_.message).mkString("\n")
        )

      stats match {
        case Nil           => untpd.EmptyTree
        case single :: Nil => single
        case many          => untpd.Block(many.init, many.last)(using source)
      }
    }
  }

  /** Walk the untyped tree. Parser-level trees are untpd, not tpd: in 3.8.3
   *  Tree is covariant (Tree[+T <: Untyped], Untyped = Type | Null), so
   *  tpd.Tree <: untpd.Tree and a parsed tree is not a tpd.Tree.
   *
   *  Parser-level shapes worth knowing:
   *    - `x + y` is an untpd.InfixOp, not an Apply (that desugaring happens later)
   *    - integer literals are untpd.Number nodes, not Literal
   */
  def traverseTree(tree: untpd.Tree)(using Context): Unit = {
    tree match {
      case v: untpd.ValDef =>
        println(s"ValDef: name = ${v.name}, rhs = ${v.rhs.show}")
        traverseTree(v.rhs)

      // Method call in already-applied form, like f(a, b)
      case a: untpd.Apply =>
        println(s"Apply: fun = ${a.fun.show}, args = ${a.args.map(_.show).mkString(", ")}")
        traverseTree(a.fun)
        a.args.foreach(traverseTree)

      // Infix operator application, like x + y
      case op: untpd.InfixOp =>
        println(s"InfixOp: left = ${op.left.show}, op = ${op.op.name}, right = ${op.right.show}")
        traverseTree(op.left)
        traverseTree(op.right)

      case i: untpd.Ident =>
        println(s"Ident: name = ${i.name}")

      // Numeric literal as produced by the parser (kept generic for
      // generic number literal support; becomes Literal during typing)
      case n: untpd.Number =>
        println(s"Number: digits = ${n.digits}, kind = ${n.kind}")

      // Non-numeric constants, e.g. strings, already arrive as Literal
      case l: untpd.Literal =>
        println(s"Literal: value = ${l.const.value}")

      case b: untpd.Block =>
        println("Block:")
        b.stats.foreach(traverseTree)
        traverseTree(b.expr)

      case other =>
        println(s"Other: ${other.getClass.getSimpleName}")
    }
  }

  /** Rewrite every numeric literal to 42. untpd.cpy preserves modifiers and
   *  spans; the raw untpd.ValDef(name, tpt, rhs) factory would drop the mods
   *  and needs an implicit SourceFile besides.
   */
  def rewriteTree(tree: untpd.Tree)(using Context): untpd.Tree = {
    tree match {
      case n: untpd.Number =>
        println(s"Rewriting Number from ${n.digits} to 42")
        untpd.Literal(Constant(42))(using n.source).withSpan(n.span)

      case b: untpd.Block =>
        println("Rewriting Block")
        untpd.cpy.Block(b)(b.stats.map(rewriteTree), rewriteTree(b.expr))

      case v: untpd.ValDef =>
        println(s"Rewriting ValDef for ${v.name}")
        untpd.cpy.ValDef(v)(v.name, v.tpt, rewriteTree(v.rhs))

      case a: untpd.Apply =>
        println("Rewriting Apply")
        untpd.cpy.Apply(a)(rewriteTree(a.fun), a.args.map(rewriteTree))

      case op: untpd.InfixOp =>
        println(s"Rewriting InfixOp ${op.op.name}")
        untpd.cpy.InfixOp(op)(rewriteTree(op.left), op.op, rewriteTree(op.right))

      case i: untpd.Ident =>
        println(s"Ident: ${i.name}")
        i

      case other =>
        println(s"Other node: ${other.getClass.getSimpleName}")
        other
    }
  }

  def main(args: Array[String]): Unit = {
    given Context = makeContext()

    // Block of Scala code we want to parse
    val code = """
      val x = 10
      val y = 20
      x + y
    """

    // Parse the code and get the AST
    val tree = getASTFromCode(code)

    // Print the AST structure
    println(s"AST of the code block:\n${tree.show}")

    traverseTree(tree)
    val newTree: untpd.Tree = rewriteTree(tree)
    val newCode = newTree.show.replaceAll("\u001b\\[[0-9;]*m", "")
    println(s"AST of the NEW code block:\n${newTree.toString}")

    // The untpd pretty-printer is the unparser
    println("Converted unparsed tree:")
    println(newCode)

    // Toolbox replacement. Since 3.8 the REPL lives in the separate
    // scala3-repl artifact, which registers a JSR-223 engine named "scala".
    val engine = new ScriptEngineManager(getClass.getClassLoader).getEngineByName("scala")
    val res2 = engine.eval(newCode)
    println(s"evaluated: $res2")
  }
}
