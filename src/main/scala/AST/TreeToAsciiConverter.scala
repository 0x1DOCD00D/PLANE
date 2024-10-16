package AST

import AST.AstFromCodeSnippet.traverseTree
import dotty.tools.dotc.ast.tpd.{Apply, Block, Ident, Literal, ValDef}
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.ast.untpd
import dotty.tools.dotc.config.Printers.{Printer, typr}

import scala.quoted.*

object TreeToAsciiConverter {

  // Function to translate Tree to a plain ASCII string without Unicode
  def treeToAscii(tree: untpd.Tree)(using ctx: Context): String = {
    tree match {
      case v: untpd.ValDef =>
        val typeStr = v.tpt match {
          case untpd.TypeTree() => treeToAscii(v.tpt) // Convert the TypeTree
          case _ => "" // If there's no type or an implicit type
        }
        val rhsStr = treeToAscii(v.rhs) // Convert the right-hand side expression
        if typeStr.isEmpty || typeStr.contains("<notype>") then
          s"val ${v.name} = $rhsStr"
        else s"val ${v.name}: $typeStr = $rhsStr"
        
      case untpd.Ident(name) =>
        // Handles identifiers
        name.toString

      case untpd.Apply(fun, args) =>
        // Handles function or method applications
        val funStr = treeToAscii(fun)
        val argsStr = args.map(treeToAscii).mkString(", ")
        s"$funStr($argsStr)"

      case untpd.Literal(constant) =>
        // Handles literal values (e.g., numbers, strings)
        constant.value.toString

      case untpd.Block(stats, expr) =>
        // Handles a block of statements
        val statsStr = stats.map(treeToAscii).mkString("\n")
        val exprStr = treeToAscii(expr)
        s"{\n$statsStr\n$exprStr\n}"

      case untpd.TypeDef(name, rhs) =>
        // Handles type definitions
        s"type $name = ${treeToAscii(rhs)}"

      case untpd.Select(qualifier, name) =>
        // Handles selections (e.g., object.field)
        s"${treeToAscii(qualifier)}.$name"

      case untpd.If(cond, thenp, elsep) =>
        // Handles if-else expressions
        val condStr = treeToAscii(cond)
        val thenStr = treeToAscii(thenp)
        val elseStr = treeToAscii(elsep)
        s"if ($condStr) $thenStr else $elseStr"

      case untpd.WhileDo(cond, body) =>
        // Handles while loops
        val condStr = treeToAscii(cond)
        val bodyStr = treeToAscii(body)
        s"while ($condStr) $bodyStr"

      case _ =>
        // For unhandled nodes, fall back to tree.show (which gives a generic representation)
        tree.show(using ctx)
    }
  }
}
