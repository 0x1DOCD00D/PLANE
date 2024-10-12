package TypeFP

import scala.compiletime.ops.any.==

object TypeGenerationCompileTime:

  import scala.compiletime.ops.int._
  import scala.compiletime.ops.boolean._
  import scala.compiletime.erasedValue

  // Define type-level tags for Positive, Negative, and Zero
  sealed trait NumberType

  sealed trait Positive extends NumberType

  sealed trait Negative extends NumberType

  sealed trait Zero extends NumberType

  // Type-level function to determine if a number is positive, negative, or zero
  type IsPositive[N <: Int] = N > 0
  type IsZero[N <: Int] = N == 0

  // Use a match type to return the appropriate NumberType based on the comparison results
  type CheckSign[N <: Int] <: NumberType = IsZero[N] match {
    case true => Zero
    case false => IsPositive[N] match {
      case true => Positive
      case false => Negative
    }
  }

  // Compile-time function to check the type-level result
  inline def checkSign[N <: Int]: String = inline erasedValue[CheckSign[N]] match {
    case _: Positive => "The number is positive."
    case _: Negative => "The number is negative."
    case _: Zero => "The number is zero."
  }

  // Testing the function
  val checkPos: String = checkSign[5] // "The number is positive."
  val checkNeg: String = checkSign[-3] // "The number is negative."
  val checkZro: String = checkSign[0] // "The number is zero."

  def main(args: Array[String]): Unit = {
    println(s"Check positive number: $checkPos") // Output: The number is positive.
    println(s"Check negative number: $checkNeg") // Output: The number is negative.
    println(s"Check zero number: $checkZro") // Output: The number is zero.
  }