package AST

import dotty.tools.dotc.*
import dotty.tools.dotc.core.Contexts.*
import dotty.tools.dotc.core.Phases.*
import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.ast.{desugar, untpd}
import dotty.tools.dotc.core.Constants.Constant
import dotty.tools.dotc.util.SourceFile
import dotty.tools.io.VirtualFile
import dotty.tools.dotc.parsing.Parsers.Parser
import ast.desugar.*

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
/*
      case v: TypeIdent  => println(v.name) 
      case v: Return => 
        println(v.expr.toString)
        println(v.from.toString)
      case Try(block, handlers, finalizer)             =>
        node(ReflectionType.Try, c(block), c(handlers), c(finalizer))
      case MatchTypeTree(bound, selector, cases)       =>
        node(ReflectionType.MatchTypeTree, c(bound), c(selector), c(cases))
      case If(cond, thenp, elsep)                      =>
        node(ReflectionType.If, c(cond), c(thenp), c(elsep))
      case Super(qual, mix)                            =>
        node(ReflectionType.Super, c(qual), c(mix))
      case TypeSelect(qualifier, name)                 =>
        node(ReflectionType.TypeSelect, c(qualifier), c(name))
      case TypeCaseDef(pat, body)                      =>
        node(ReflectionType.TypeCaseDef, c(pat), c(body))
      case TypeProjection(qualifier, name)             =>
        node(ReflectionType.TypeProjection, c(qualifier), c(name))
      case Repeated(elems, elemtpt)                    =>
        node(ReflectionType.Repeated, c(elems), c(elemtpt))
      case Typed(expr, tpt)                            =>
        node(ReflectionType.Typed, c(expr), c(tpt))
      case Annotated(arg, annot)                       =>
        node(ReflectionType.Annotated, c(arg), c(annot))
      case This(qual)                                  =>
        node(ReflectionType.This, c(qual))
      case Singleton(ref)                              =>
        node(ReflectionType.Singleton, c(ref))
      case PackageClause(pid, stats)                   =>
        node(ReflectionType.PackageClause, c(pid), c(stats))
      case CaseDef(pat, guard, body)                   =>
        node(ReflectionType.CaseDef, c(pat), c(guard), c(body))
      case Inlined(call, bindings, expansion)          =>
        node(ReflectionType.Inlined, c(call), c(bindings), c(expansion))
      case DefDef(name, paramsClauses, returnTpt, rhs) =>
        node(ReflectionType.DefDef, c(name), c(paramsClauses), c(returnTpt), c(rhs))
      case ClassDef(name, constr, parents, self, body) =>
        node(ReflectionType.ClassDef, c(name), c(constr), c(parents), c(self), c(body))
      case Export(expr, selectors)                     =>
        node(ReflectionType.Export, c(expr), c(selectors))
      case WildcardTypeTree()                          =>
        node(ReflectionType.WildcardTypeTree)
      case TypedOrTest(tree, tpt)                      =>
        node(ReflectionType.TypedOrTest, c(tree), c(tpt))
      case TypeApply(fun, args)                        =>
        node(ReflectionType.TypeApply, c(fun), c(args))
      case ByName(result)                              =>
        node(ReflectionType.ByName, c(result))
      case Literal(const)                              =>
        node(ReflectionType.Literal, c(const))
      case Inferred()                                  =>
        node(ReflectionType.Inferred)
      case LambdaTypeTree(tparams, body)               =>
        node(ReflectionType.LambdaTypeTree, c(tparams), c(body))
      case Alternatives(patterns)                      =>
        node(ReflectionType.Alternatives, c(patterns))
      case TypeBind(name, bounds)                      =>
        node(ReflectionType.TypeBind, c(name), c(bounds))
      case TypeDef(name, rhs)                          =>
        node(ReflectionType.TypeDef, c(name), c(rhs))
      case Apply(fun, args)                            =>
        node(ReflectionType.Apply, c(fun), c(args))
      case Applied(tpt, args)                          =>
        node(ReflectionType.Applied, c(tpt), c(args))
      case TypeBoundsTree(lo, hi)                      =>
        node(ReflectionType.TypeBoundsTree, c(lo), c(hi))
      case Wildcard()                                  =>
        node(ReflectionType.Wildcard)
      case While(cond, body)                           =>
        node(ReflectionType.While, c(cond), c(body))
      case Unapply(fun, implicits, patterns)           =>
        node(ReflectionType.Unapply, c(fun), c(implicits), c(patterns))
      case Import(expr, selectors)                     =>
        node(ReflectionType.Import, c(expr), c(selectors))
      case Refined(tpt, refinements)                   =>
        node(ReflectionType.Refined, c(tpt), c(refinements))
      case SummonFrom(cases)                           =>
        node(ReflectionType.SummonFrom, c(cases))
      case Bind(name, body)                            =>
        node(ReflectionType.Bind, c(name), c(body))
      case ValDef(name, tpt, rhs)                      =>
        node(ReflectionType.ValDef, c(name), c(tpt), c(rhs))
      case New(tpt)                                    =>
        node(ReflectionType.New, c(tpt))
      case NamedArg(name, arg)                         =>
        node(ReflectionType.NamedArg, c(name), c(arg))
      case Select(qualifier, name)                     =>
        node(ReflectionType.Select, c(qualifier), c(name))
      case Match(selector, cases)                      =>
        node(ReflectionType.Match, c(selector), c(cases))
      case Closure(meth, tpt)                          =>
        node(ReflectionType.Closure, c(meth), c(tpt))
      case Block(stats, expr)                          =>
        node(ReflectionType.Block, c(stats), c(expr))
      case TypeBlock(aliases, tpt)                     =>
        node(ReflectionType.TypeBlock, c(aliases), c(tpt))
      case Assign(lhs, rhs)                            =>
        node(ReflectionType.Assign, c(lhs), c(rhs))
      case Ident(name)                                 =>
        node(ReflectionType.Ident, c(name))
      
      
*/
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
    import scala.reflect.runtime.universe._
    import scala.tools.reflect.ToolBox
    // Initialize a basic context with default settings
    implicit val ctx: Context = (new ContextBase).initialCtx.fresh

    // Block of Scala code we want to parse
    val code = """
      val x = 10
      val y = 20
      x + y
    """

    val mirror = runtimeMirror(this.getClass.getClassLoader)
    val toolbox = mirror.mkToolBox()
    val blockOld = toolbox.parse(code)
    val res1 = toolbox.eval(blockOld)
    println(s"evaluated: $res1")

    // Parse the code and get the AST
    val tree = getASTFromCode(code)

    // Print the AST structure
    println(s"AST of the code block:\n${tree.show}")

    traverseTree(tree)
    val newTree: untpd.Tree = rewriteTree(tree)
    println(s"AST of the NEW code block:\n${newTree.toString}")

    val newCode = TreeToAsciiConverter.treeToAscii(newTree)
    println("Converted unparsed tree:")
    println(newCode)
    val blockNew = toolbox.parse(newCode)
    val res2 = toolbox.eval(blockNew)
    println(s"evaluated: $res2")

  }
}
