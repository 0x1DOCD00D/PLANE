
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.StateManipulations

object MonteCarloRNG:
  //> using scala "3.3.1"
  //> using dep "org.typelevel:cats-core_3:2.12.0"

  import cats.data.State
  import cats.syntax.all.*

  // -----------------------------
  // Simple 64-bit LCG RNG: X_{n+1} = (a * X_n + c) mod 2^64
  // We rely on Long overflow to get modulo 2^64 semantics.
  // -----------------------------
  final case class RNG(seed: Long)

  type R[A] = State[RNG, A]

  private val A: Long = 6364136223846793005L
  private val C: Long = 1442695040888963407L

  def nextLong: R[Long] =
    State { r =>
      val s1 = r.seed * A + C
      (RNG(s1), s1)
    }

  def nextDouble01: R[Double] =
    nextLong.map { x =>
      // Take top 53 bits and scale to [0,1). Deterministic given seed.
      val bits = (x >>> 11) & ((1L << 53) - 1)
      bits.toDouble / (1L << 53).toDouble
    }

  // One random point in unit square.
  def randomPoint2D: R[(Double, Double)] =
    for {
      x <- nextDouble01
      y <- nextDouble01
    } yield (x, y)

  // Is (x, y) in quarter circle of radius 1.
  def insideUnitQuarterCircle(p: (Double, Double)): Boolean =
    val (x, y) = p
    x * x + y * y <= 1.0

  // Estimate π using N samples: π ≈ 4 * (#inside / N).
  def estimatePi(n: Int): R[Double] =
    for {
      ps <- List.fill(n)(randomPoint2D).sequence
      hits = ps.count(insideUnitQuarterCircle)
    } yield 4.0 * hits.toDouble / n.toDouble

  // “Behind the scenes” note:
  // All of the above is just composing RNG => (RNG, A) functions.
  // There is no hidden global. Same seed => identical sequence.

  @main def runMonteCarloRNG(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Cats/StateManipulations/MonteCarloRNG.scala created at time 1:54PM")
    val s0 = RNG(seed = 42L)
    val (s1, pi1) = estimatePi(200000).run(s0).value
    val (s2, pi2) = estimatePi(200000).run(s0).value // same seed => same result

    println(f"π1 = $pi1%.5f   π2 = $pi2%.5f   same-seed deterministic = ${pi1 == pi2}")
    println(s"seed progressed to ${s1.seed} (but we never mutated anything)")

