package TypeFP

import scala.compiletime.ops.int._

object ProofTypeArithmetic:

  def proof[P1 <: Int, P2 <: Int, P3 <: Int](p1: P1, p2:P2)(using ev: (P1 + P2) =:= P3): Int = p1+p2

  val proofResult: (3, 4) => Int = proof[3, 4, 7]

  def main(args: Array[String]): Unit = {
    println(s"Proof result: ${proofResult(3,4)}")
  }
