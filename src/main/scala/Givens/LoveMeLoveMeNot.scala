
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Givens

object LoveMeLoveMeNot:

  trait A                                     // our proposition A

  /*  ¬A – a single implicit that turns any proof of A into ⊥                */
  given notA: (A => Nothing) = (a: A) => throw new Exception("💥Boom – contradiction!")

  /* ex-falso-quodlibet: from A ∧ ¬A we get ⊥, from ⊥ we get any T           */
//  https://www.merriam-webster.com/dictionary/quodlibet
  inline def absurd[T](using a: A, na: A => Nothing): T = na(a)

  import scala.util.{Try, Success, Failure}

  private def show[T](label: String)(expr: => T): Unit =
    Try(expr) match
      case Success(v) => println(f"$label%-20s ⇒ $v (should be impossible)")
      case Failure(e) => println(f"$label%-20s ⇒ 💥${e.getMessage}")

  @main def main(): Unit =
    given proofOfA: A = new A {}             // now A and ¬A both hold

    show("Int from ⊥")          { absurd[Int] }
    show("String from ⊥")       { absurd[String] }
    show("List[Double] from ⊥") { absurd[List[Double]] }
    show("Boolean ‘1==2’")      { absurd[Boolean] }
    show("Function from ⊥") {
      val f: (Int, Int) => Int = absurd
      f(6, 7)
    }
//Every call to `absurd` type-checks because `na(a)` has type `Nothing`, which the compiler can widen to any requested `T`.
//At run-time each expression throws the *Boom – contradiction!* exception, perfectly illustrating **ex-falso quodlibet**: from a logical contradiction you can derive anything—just not safely use it.
    // 1. Type-equality evidence between two *different* types
    show("Evidence Int =:= String") {
      val ev: Int =:= String = absurd
      ev // already explodes
    }

    // 2. A bogus Numeric instance for String
    show("Numeric[String] instance") {
      val num: Numeric[String] = absurd
      num.plus("one", "two") // unreachable
    }

    // 3. A Future magically materialised from nowhere
    show("Future[Int]") {
      import scala.concurrent.*, duration.*
      given ec: ExecutionContext = ExecutionContext.global

      val f: Future[Int] = absurd
      Await.result(f, 100.millis)
    }

    // 4. A JDBC connection out of thin air
    show("java.sql.Connection") {
      val conn: java.sql.Connection = absurd
      conn.isClosed
    }

    // 5. A Mirror for a tuple that we never derived
    show("Mirror.ProductOf[(Int, String)]") {
      import scala.deriving.Mirror
      val m: Mirror.ProductOf[(Int, String)] = absurd
      m
    }

    // 6. A fully-fledged Ordering for functions
    show("Ordering[(Int) => Int]") {
      val ord: Ordering[Int => Int] = absurd
      ord.compare(_ + 1, _ + 2)
    }

    // 7. A proof that 0 == 1 in Peano arithmetic style
    type Zero
    type Succ[N]

    given zeroIsSuccOne: (Zero =:= Succ[Zero]) = absurd

    show("Evidence Zero =:= Succ[Zero]") {
      zeroIsSuccOne
    }

    // 8. An Option[A] where A is the *very* proposition being contradicted
    show("Option[A]") {
      absurd[Option[A]]
    }

    // 9. A function that promises never to return but has a concrete result type
    show("Int-returning diverging function") {
      val f: () => Int = absurd
      f()
    }

    // 10. An implicit Numeric[Double] *shadow* conjured at call site
    show("Implicit shadow Numeric[Double]") {
      given bogus: Numeric[Double] = absurd

      implicitly[Numeric[Double]].fromInt(42)
    }
