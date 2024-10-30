package TypeFP

object PeanoSumWithGiven:
  sealed trait Nat

  sealed trait _0 extends Nat

  sealed trait Succ[N <: Nat] extends Nat

  trait Sum[A <: Nat, B <: Nat] {
    type Result <: Nat
  }

  object Sum {
    type Aux[A <: Nat, B <: Nat, R <: Nat] = Sum[A, B] {type Result = R}

    // Base case: 0 + B = B
    given sum0[B <: Nat]: Sum.Aux[_0, B, B] = new Sum[_0, B] {
      type Result = B
    }

    // Recursive case: Succ[A] + B = Succ[A + B]
    given sumSucc[A <: Nat, B <: Nat](using sumAB: Sum[A, B]): Sum.Aux[Succ[A], B, Succ[sumAB.Result]] =
    new Sum[Succ[A], B] {
      type Result = Succ[sumAB.Result]
    }
  }

  trait Proof[A <: Nat, B <: Nat, Expected <: Nat]

  object Proof {
    // Proof that A + B = Expected
    given proofSum[A <: Nat, B <: Nat, Expected <: Nat](using sum: Sum.Aux[A, B, Expected]): Proof[A, B, Expected] =
    new Proof[A, B, Expected] {}
  }

  def main(args: Array[String]): Unit = {
    // Some type-level numbers
    type _1 = Succ[_0]
    type _2 = Succ[_1]
    type _3 = Succ[_2]
    type _4 = Succ[_3]

    // Summon proof that 1 + 2 = 3
    val proof1Plus2: Proof[_1, _2, _3] = summon[Proof[_1, _2, _3]]

    // Summon proof that 2 + 2 = 4
    val proof2Plus2: Proof[_2, _2, _4] = summon[Proof[_2, _2, _4]]

    // Uncommenting the following line would cause a compile-time error
    // because 1 + 2 != 4
//      val wrong1: Proof[_1, _2, _4] = summon[Proof[_1, _2, _4]]
//      val wrong2: Proof[_0, _3, _4] = summon[Proof[_0, _3, _4]]
  }