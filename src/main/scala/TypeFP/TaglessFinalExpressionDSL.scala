package TypeFP

object TaglessFinalExpressionDSL {
  trait ExprAlg[F[_]] {
    def lit(n: Int): F[Int]

    def add(x: F[Int], y: F[Int]): F[Int]
  }

  type Id[A] = A

  given evalInterpreter: ExprAlg[Id] with
    def lit(n: Int): Int = n
    def add(x: Int, y: Int): Int = x + y

  given printInterpreter: ExprAlg[[A] =>> String] with
    def lit(n: Int): String = n.toString
    def add(x: String, y: String): String = s"($x + $y)"

  def program[F[_] : ExprAlg]: F[Int] = {
    val e = summon[ExprAlg[F]]
    e.add(e.lit(10), e.add(e.lit(20), e.lit(30)))
  }

  def main(args: Array[String]): Unit = {
    println("Tagless Final Expression DSL Example")

    val evaluated: Int = program[Id]
    val printed: String = program[[A] =>> String]

    println(s"Expression 1: $evaluated")
    println(s"Expression 2: $printed")
  }
}
