
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.StateManipulations

object CollatzNumbers:
  //> using scala "3.3.1"
  //> using dep "org.typelevel:cats-core_3:2.12.0"

  import cats.data.State
  import cats.syntax.all.*
  import cats.Monad

  /*
   * Collatz with Cats State (Variant A: Monad[C].tailRecM)
   * ------------------------------------------------------
   * State[S, A] ≅ S => (S, A)
   * S = Ctx(seen, seq), A varies.
   */

  final case class Ctx(seen: Set[BigInt], seq: Vector[BigInt])

  type C[A] = State[Ctx, A]

  def collatzNext(n: BigInt): BigInt =
    if (n % 2 == 0) n / 2 else 3 * n + 1

  /** Append n to the sequence and mark it as seen. */
  def push(n: BigInt): C[Unit] =
    State.modify(ctx => ctx.copy(seen = ctx.seen + n, seq = ctx.seq :+ n))

  /** One step for tailRecM: continue with Left(next) or stop with Right(()). */
  //Advance by one Collatz hop, record it, and say whether to keep going
  def step(curr: BigInt): C[Either[BigInt, Unit]] =
    State { ctx0 =>
      val nxt = collatzNext(curr)
      val repeated = ctx0.seen.contains(nxt)
      val ctx1 = ctx0.copy(seen = ctx0.seen + nxt, seq = ctx0.seq :+ nxt)
      val out: Either[BigInt, Unit] =
        if (nxt == 1 || repeated) Right(()) else Left(nxt)
      (ctx1, out)
    }

  /** Build full sequence from `start`, stopping at 1 or first repeat. */
  def collatzUntilRepeatOrOne(start: BigInt): C[Vector[BigInt]] =
    require(start > 0, "start must be positive")
    for {
      _ <- push(start) // seed exactly once
      _ <- if (start == 1) State.pure[Ctx, Unit](())
        /*
          Monad[C].tailRecM[A, B](a)(f) : C[B]
          here: A = BigInt, B = Unit, a = start, f = step
          f: A => C[Either[A, B]]
          Start with seed a.
          Repeatedly run f(a) in the same monad.
          If f(a) yields Left(a2), loop with a := a2.
          If it yields Right(b), stop and return b.
        * */
      else Monad[C].tailRecM(start)(step)
      seq <- State.inspect[Ctx, Vector[BigInt]](_.seq)
    } yield seq

  /*
    tailRecM as Run step repeatedly, threading state, until step says Right.
    def tailRecM(start: BigInt)(step: BigInt => State[Ctx, Either[BigInt, Unit]]) : State[Ctx, Unit] =
      State { s0 =>
        var s = s0
        var a = start
        var done = false
        while (!done) {
          val (s1, eab) = step(a).run(s).value  // run one stateful step
          s = s1                                 // thread state forward
          eab match {
            case Left(next) => a = next          // continue
            case Right(())  => done = true       // stop
          }
        }
        (s, ())                                   // final state, result
    }
  * */

  /** Run with an empty initial context, since we push(start) inside. */
  def runCollatz(start: BigInt): Vector[BigInt] =
    collatzUntilRepeatOrOne(start)
      .runA(Ctx(seen = Set.empty, seq = Vector.empty))
      .value

  @main def runCollatzNumbers(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Cats/StateManipulations/CollatzNumbers.scala created at time 2:10PM")
    val start = BigInt(27) // try 6, 7, 27, 97, etc.
    val seq = runCollatz(start)

    println(s"start = $start")
    println(s"length = ${seq.length}")
    println(seq.mkString(" → "))

// Behind the scenes:
// - State[S, A] ≅ S => (S, A)
// - `push` is just: s => (s.copy(...), ())
// - `step` is: s => (s1, eitherNextOrDone)
// - tailRecM threads S through those functions in a stack-safe way.

