package AST

import dotty.tools.dotc.*
import dotty.tools.dotc.core.Contexts.*
import dotty.tools.dotc.core.Phases.*
import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.ast.untpd
import dotty.tools.dotc.core.Constants.Constant
import dotty.tools.dotc.util.SourceFile
import dotty.tools.io.VirtualFile
import dotty.tools.dotc.parsing.Parsers.Parser
import dotty.tools.dotc.reporting.Reporter
import dotty.tools.dotc.typer.Typer

object AstFromCodeSnippet {

  // Function to parse and return the AST from a block of code
  def getASTFromCode(code: String)(implicit ctx: Context): Tree = {
    val virtualFile = new VirtualFile("dummy.scala")
    val writer = virtualFile.output
    writer.write(code.getBytes)
    writer.close()
    new Parser(new SourceFile(virtualFile, code.toCharArray)).block() // Parse the code block into an AST
  }

  def traverseTree(tree: Tree): Unit = {
    import AST.BasicManipulations.implicitCtx
    tree match {
      // Match a ValDef node (variable declaration)
      case v: ValDef =>
        println(s"ValDef: name = ${v.name}, rhs = ${v.rhs.show}")
        traverseTree(v.rhs) // Traverse the right-hand side (rhs) of the definition

      // Match an Apply node (method call, like x + y)
      case a: Apply =>
        println(s"Apply: fun = ${a.fun.show}, args = ${a.args.map(_.show).mkString(", ")}")
        traverseTree(a.fun) // Traverse the function being applied
        a.args.foreach(traverseTree) // Traverse each argument in the method call

      // Match an Ident node (identifier, like variable names)
      case i: Ident =>
        println(s"Ident: name = ${i.name}")

      // Match a Literal node (constants like 42)
      case l: Literal =>
        println(s"Literal: value = ${l.const.value}")

      // Match a block (a sequence of statements or expressions)
      case b: Block =>
        println("Block:")
        b.stats.foreach(traverseTree) // Traverse all statements in the block
        traverseTree(b.expr) // Traverse the expression at the end of the block

      // If no specific match, just print the tree
      case other =>
        println(s"Other: ${other.getClass.getSimpleName}")
    }
  }

  def rewriteTree(tree: untpd.Tree)(implicit ctx: Context): untpd.Tree = {
    tree match {
      case n if n.getClass.getSimpleName == "Number" =>
        println(s"Rewriting Number from ${n.toString} to 42")
        untpd.Literal(Constant(42))

      // Match and rewrite blocks (sequences of expressions/statements)
      case b: untpd.Block =>
        println("Rewriting Block")
        val rewrittenStats = b.stats.map(rewriteTree)  // Traverse all block statements
        val rewrittenExpr = rewriteTree(b.expr)        // Traverse the final expression
        untpd.Block(rewrittenStats, rewrittenExpr)

      // Match and rewrite ValDef nodes (variable definitions)
      case v: untpd.ValDef =>
        println(s"Rewriting ValDef for ${v.name}")
        val rewrittenRhs = rewriteTree(v.rhs)          // Traverse and rewrite the RHS
        untpd.ValDef(v.name, v.tpt, rewrittenRhs)             // Return a new ValDef

      // Match and rewrite Apply nodes (method calls, like x + y)
      case a: untpd.Apply =>
        println("Rewriting Apply")
        val rewrittenFun = rewriteTree(a.fun)          // Traverse and rewrite the function part
        val rewrittenArgs = a.args.map(rewriteTree)    // Traverse and rewrite the arguments
        untpd.Apply(rewrittenFun, rewrittenArgs)              // Return a new Apply node

      // Match and rewrite Ident nodes (identifiers, like variable names x or y)
      case i: untpd.Ident =>
        println(s"Ident: ${i.name}")
        i  // Leave the identifier unchanged

      // Match and recursively rewrite any other tree nodes
      case other =>
        println(s"Other node: ${other.getClass.getSimpleName}")
        other
    }
  }

  def main(args: Array[String]): Unit = {
    // Initialize a basic context with default settings
    implicit val ctx: Context = (new ContextBase).initialCtx.fresh

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
    val newTree = rewriteTree(tree)
    println(s"AST of the NEW code block:\n${newTree.show}")
  }
}
