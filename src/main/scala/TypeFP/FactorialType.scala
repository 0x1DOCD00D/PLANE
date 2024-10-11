package TypeFP

object FactorialType:

  import scala.compiletime.ops.int._
  import scala.compiletime.{constValue, summonInline}

  // Define a type-level factorial using match types and recursion
  type Factorial[N <: Int] <: Int = N match {
    case 0 => 1 // Base case: Factorial[0] = 1
    case _ => N * Factorial[N - 1] // Recursive case: Factorial[N] = N * Factorial[N - 1]
  }

  // Compile-time check for factorial using `constValue`
  inline def checkFactorial[N <: Int, Expected <: Int]: Boolean =
    constValue[Factorial[N]] == constValue[Expected]

  // Now let's compute the factorial of 5 at compile time
  val fact5:120 = constValue[Factorial[5]]
  println(fact5)
  val fact5Check: Boolean = checkFactorial[5, 120] // Factorial[5] = 120

  def main(args: Array[String]): Unit = {
    println(s"Factorial of 5 is $fact5? $fact5Check")

  }