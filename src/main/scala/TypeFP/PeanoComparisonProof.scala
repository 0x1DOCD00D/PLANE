package TypeFP

import scala.compiletime.constValue

object PeanoComparisonProof:
  sealed trait Nat
  case object Zero extends Nat
  case class Succ[N <: Nat](n: N) extends Nat

  type Compare[N <: Nat, M <: Nat] <: String = (N, M) match {
    case (Zero.type, Zero.type) => "Equal"
    case (Zero.type, Succ[_]) => "Less"
    case (Succ[_], Zero.type) => "Greater"
    case (Succ[n1], Succ[m1]) => Compare[n1, m1] // Recursively compare successors
  }

  type _0 = Zero.type
  type _1 = Succ[_0]
  type _2 = Succ[_1]
  type _3 = Succ[_2]

  // Proof: 1 < 2
  val proof1 = constValue[Compare[_1, _2]] // Should resolve to "Less"

  // Proof: 2 > 1
  val proof2 = constValue[Compare[_2, _1]] // Should resolve to "Greater"

  // Proof: 2 == 2
  val proof3 = constValue[Compare[_2, _2]] // Should resolve to "Equal"

  def main(args: Array[String]): Unit = {
    println(s"1 compared to 2: $proof1") // Output: Less
    println(s"2 compared to 1: $proof2") // Output: Greater
    println(s"2 compared to 2: $proof3") // Output: Equal

  }