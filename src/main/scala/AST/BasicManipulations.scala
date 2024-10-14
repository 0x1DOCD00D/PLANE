package AST

import dotty.tools.dotc._
import dotty.tools.dotc.core.Contexts._
import dotty.tools.dotc.core.Constants._
import dotty.tools.dotc.ast.tpd._
import dotty.tools.dotc.reporting._
import dotty.tools.dotc.interfaces._

object BasicManipulations:
  val driver = new Driver
  val ctx: Context = (new ContextBase).initialCtx.fresh

  // Now we can use the context
  given implicitCtx: Context = ctx

  // Create an AST node representing the literal value 42
  val literal42: Tree = Literal(Constant(42))(using implicitCtx)  
  def main(args: Array[String]): Unit = {
    println(s"AST of the literal: ${literal42.show}")
  }