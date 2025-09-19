
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package TypeFP

object NatIndexedVectors:
  // ---------- Peano naturals ----------
  sealed trait Nat

  sealed trait Z extends Nat

  sealed trait S[N <: Nat] extends Nat

  // ---------- Curried match-type addition (recurse on the *first* arg) ----------
  // Plus[X, Y] reduces as:
  //   Plus[Z,    Y] = Y
  //   Plus[S[x], Y] = S[Plus[x, Y]]
  type Plus[X <: Nat, Y <: Nat] <: Nat = X match
    case Z => Y
    case S[x] => S[Plus[x, Y]]

  type ++[X <: Nat, Y <: Nat] = Plus[X, Y]

  // ---------- Length-indexed vectors ----------
  sealed trait Vec[+A, N <: Nat]

  case object VNil extends Vec[Nothing, Z]

  final case class VCons[A, N <: Nat](head: A, tail: Vec[A, N]) extends Vec[A, S[N]]

  // ---------- Normalization lemmas (compiler-derived) ----------

  import scala.compiletime.summonInline

  // We ask the compiler for equalities in the *direction we need* for coersions.
  inline given plusZeroRight[N <: Nat]: (N =:= Plus[Z, N]) =
    summonInline[(N =:= Plus[Z, N])] // since Plus[Z, N] reduces to N

  inline given plusSuccRight[h <: Nat, N <: Nat]: (S[Plus[h, N]] =:= Plus[S[h], N]) =
    summonInline[(S[Plus[h, N]] =:= Plus[S[h], N])] // since Plus[S[h], N] reduces to S[Plus[h, N]]

  // A tiny, type-safe (under the equality) index rewrite for vectors.
  // Scala 3's `=:=` does not ship `symm`/`subst`, so we encode the coercion directly.
  private inline def coeVec[A, X <: Nat, Y <: Nat](v: Vec[A, X])(using ev: X =:= Y): Vec[A, Y] =
    v.asInstanceOf[Vec[A, Y]] // safe by `ev` (definitional equality)

  // ---------- Operations ----------
  def head[A, N <: Nat](v: Vec[A, S[N]]): A = v match
    case VCons(h, _) => h

  def vmap[A, B, N <: Nat](v: Vec[A, N])(f: A => B): Vec[B, N] = v match
    case VNil => VNil
    case VCons(h, t) => VCons(f(h), vmap(t)(f))

  // Append: match on `xs` so the branch index is Plus[S[m], N] which reduces to S[Plus[m, N]].
  def append[A, M <: Nat, N <: Nat](xs: Vec[A, M], ys: Vec[A, N]): Vec[A, ++[M, N]] =
    xs match
      case VNil =>
        // Expected: Vec[A, Plus[Z, N]]. We have Vec[A, N].
        coeVec[A, N, Plus[Z, N]](ys)(using plusZeroRight[N])
      case VCons(h, t) =>
        // When M = S[m], we know xs: Vec[A, S[m]] and t: Vec[A, m]
        // IH: append(t, ys) has type Vec[A, Plus[m, N]]
        val tail = append(t, ys)
        // Build VCons(h, tail): Vec[A, S[Plus[m, N]]]
        val built = VCons(h, tail)
        // We need Vec[A, Plus[S[m], N]] = Vec[A, Plus[M, N]]
        // Since Plus[S[m], N] reduces to S[Plus[m, N]], we need the coercion
        built.asInstanceOf[Vec[A, Plus[M, N]]]

  @main def runNatIndexedVectors(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/TypeFP/NatIndexedVectors.scala created at time 5:45PM")
    type _0 = Z
    type _1 = S[_0]
    type _2 = S[_1]
    type _3 = S[_2]
    type _4 = S[_3]
    type _5 = S[_4]

    val v3: Vec[Int, _3] = VCons(1, VCons(2, VCons(3, VNil)))
    val v2: Vec[Int, _2] = VCons(4, VCons(5, VNil))

    val v5: Vec[Int, _5] = append(v3, v2) // compiles; length reduces to 5
    val first: Int = head(v5) // 1
    val doubled: Vec[Int, _5] = vmap(v5)(_ * 2)
    println(first)
    println(doubled) // VCons(2, VCons(4, VCons(6, VCons(8, VCons(10, VNil)))))
    println(append(doubled, VNil)) // VCons(2, VCons(4, VCons(6, VCons(8, VCons(10, VNil)))))
