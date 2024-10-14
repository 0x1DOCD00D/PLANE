package AST

import dotty.tools.dotc._
import dotty.tools.dotc.core.Contexts._
import dotty.tools.dotc.core.Phases._
import dotty.tools.dotc.ast.tpd._
import dotty.tools.dotc.util.SourceFile
import dotty.tools.io.VirtualFile
import dotty.tools.dotc.parsing.Parsers.Parser
import dotty.tools.dotc.reporting.Reporter

object AstFromCodeSnippet {

  // Function to parse and return the AST from a block of code
  def getASTFromCode(code: String)(implicit ctx: Context): Tree = {
    // Create a virtual file with the Scala code as content
    val virtualFile = new VirtualFile("dummy.scala")
    val writer = virtualFile.output
    writer.write(code.getBytes)
    writer.close()

    // Create a source file from the virtual file
    val sourceFile = new SourceFile(virtualFile, code.toCharArray)

    // Use the Scala 3 parser to parse the source file and produce an AST
    val parser = new Parser(sourceFile)
    val tree = parser.block() // Parse the code block into an AST

    tree // Return the AST
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

  def main(args: Array[String]): Unit = {
    // Initialize a basic context with default settings
    implicit val ctx: Context = (new ContextBase).initialCtx.fresh

    // Block of Scala code we want to parse
    val code =
      """
        val x = 10
        val y = 20
        x + y
      """

    // Parse the code and get the AST
    val tree = getASTFromCode(code)

    // Print the AST structure
    println(s"AST of the code block:\n${tree.show}")

    traverseTree(tree)
  }
}
