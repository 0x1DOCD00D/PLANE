package algorithms

object TheExpressionProblem:
  type BasicType = Int
  enum Expression:
    case Var(s: String)
    case Val(v: BasicType)
    case Add(arg1: Expression, arg2: Expression)
    private var Env: Map[String, BasicType] = Map("x" -> 1, "y" -> 8)
    def eval: BasicType =
      this match {
        case Add(p1, p2) => p1.eval + p2.eval
        case Val(v) => v
        case Var(v) => Env(v)
      }

  @main def runExpressionProblem(): Unit =
    import Expression.*
    val exp = Add(Add(Val(2), Var("y")), Add(Val(1),Val(9)))
    println(exp.eval)
