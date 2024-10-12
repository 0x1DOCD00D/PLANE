package TypeFP

import scala.compiletime.ops.any.==
import scala.compiletime.ops.boolean.&&

object GcdProof:

  import scala.compiletime.ops.int._
  import scala.compiletime.constValue

  // Step 1: Define a type-level GCD computation using Euclid's algorithm
  // Base case: If B == 0, GCD(A, B) is A
  type GCD[A <: Int, B <: Int] <: Int = B match {
    case 0 => A
    case _ => GCD[B, A % B] // Recursive case: gcd(b, a % b)
  }

  // Step 2: Define a type-level check to verify that GCD divides A and B

  // Type-level check that GCD(A, B) divides both A and B
  type Divides[A <: Int, B <: Int] = (B % A == 0)

  // Prove that the GCD divides both A and B
  type ProveGCD[A <: Int, B <: Int] = Divides[GCD[A, B], A] && Divides[GCD[A, B], B]

  // Step 3: Extract the computed GCD at compile time using constValue
  inline def computeGCD[A <: Int, B <: Int]: Int =
    constValue[GCD[A, B]]

  // Inline function to check if the proof holds for the GCD
  inline def verifyGCD[A <: Int, B <: Int]: Boolean =
    constValue[ProveGCD[A, B]]

  // Step 4: Compile-time checks and proofs

  val gcdOf48And18 = computeGCD[48, 18] // Should compute GCD(48, 18) = 6
  val gcdOf56And98 = computeGCD[56, 98] // Should compute GCD(56, 98) = 14
  val gcdOf101And103 = computeGCD[101, 103] // Should compute GCD(101, 103) = 1

  val proveGCD1 = verifyGCD[48, 18] // GCD of 48 and 18 is 6, should be true
  val proveGCD2 = verifyGCD[56, 98] // GCD of 56 and 98 is 14, should be true
  val proveGCD3 = verifyGCD[101, 103] // GCD of 101 and 103 is 1, should be true

  def main(args: Array[String]): Unit = {
    println(s"GCD of 48 and 18: $gcdOf48And18") // 6
    println(s"GCD of 56 and 98: $gcdOf56And98") // 14
    println(s"GCD of 101 and 103: $gcdOf101And103") // 1

    println(s"Prove GCD of 48 and 18: $proveGCD1") // true
    println(s"Prove GCD of 56 and 98: $proveGCD2") // true
    println(s"Prove GCD of 101 and 103: $proveGCD3") // true
  }