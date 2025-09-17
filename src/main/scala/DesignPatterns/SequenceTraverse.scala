
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package DesignPatterns

/*
           ┌──────────────────────────────┐
A ───────► │     F[_]  (effects)         │
           │ unit / flatMap / map / map2 │
           └──────────────┬──────────────┘
                          │  (transforms inside F, sequences effects)
                          ▼
                         F[B]


           ┌──────────────────────────────┐
A ───────► │     G[_]  (container)       │
           │ Stepper(iterator) / ConsK   │
           └──────────────┬──────────────┘
          (iterate G)     │      (rebuild G)
               ▲          │          ▼
           iterator       │        cons/empty


unit : A => F[A]
flatMap : F[A] => (A => F[B]) => F[B]
map : F[A] => (A => B) => F[B]
map2 : (F[A], F[B]) => ((A,B) => C) => F[C]
iterator : G[A] => Iterator[A]
empty : G[A]
cons : (A, G[A]) => G[A]
*
* */
trait SequenceTraverse[F[_]]:
  def unit[A](a: A): F[A]
  def map[A, B](ma: F[A])(f: A => B): F[B] =
    flatMap(ma)(a => unit(f(a)))
  def map2[A, B, C](ma: F[A], mb: F[B])(f: (A, B) => C): F[C] =
    flatMap(ma)(a => map(mb)(b => f(a, b)))
  def flatMap[A, B](ma: F[A])(f: A => F[B]): F[B]

trait Stepper[G[_]]:
  def iterator[A](ga: G[A]): Iterator[A]

trait ConsK[G[_]]:
  def empty[A]: G[A]
  def cons[A](h: A, t: G[A]): G[A]

/*
Input types:
  ga : G[A]
  z  : B
  step : (A, B) => F[B]
Using:
  Stepper[G].iterator  (to read G[A])
  SequenceTraverse[F]  (unit / flatMap)
Process (stack-safe, no recursion):

G[A] --Stepper--> [a0, a1, ..., a(n-1)]  (buffered)
                          │
                          ▼  (right-to-left)
acc0 = unit(z) : F[B]
for i = n-1 downto 0:
  acc(i+1) = flatMap(acc(i)) { b => step(a_i, b) }

Result: acc(n) : F[B]

* */
def foldRightMStackSafe[G[_], F[_], A, B](
                                           ga: G[A]
                                         )(z: B)(
                                           step: (A, B) => F[B]
                                         )(using S: SequenceTraverse[F], Gs: Stepper[G]): F[B] =
  import scala.collection.mutable.ArrayBuffer
  // 1) Collect elements left-to-right
  val buf:ArrayBuffer[A] = ArrayBuffer.empty[A]
  val it: Iterator[A] = Gs.iterator(ga)
  while it.hasNext do buf += it.next()
  // 2) Fold from right to left, sequencing with flatMap
  var i:Int   = buf.length - 1
  var acc: F[B] = S.unit(z): F[B]
  while i >= 0 do
    val a = buf(i)
    acc = S.flatMap(acc)(b => step(a, b))
    i -= 1
  acc

object GivenInstances:
  given ConsK[List] with
    def empty[A] = List.empty[A]
    def cons[A](h: A, t: List[A]): List[A] = (h :: t).asInstanceOf

  given Stepper[List] with
    def iterator[A](ga: List[A]) = ga.iterator

  given SequenceTraverse[List] with
    def unit[A](a: A): List[A] = List(a)
    def flatMap[A, B](ma: List[A])(f: A => List[B]): List[B] =
      for ma <- ma; mb <- f(ma) yield mb

  given SequenceTraverse[Vector] with
    def unit[A](a: A): Vector[A] = Vector(a)
    def flatMap[A, B](ma: Vector[A])(f: A => Vector[B]): Vector[B] =
      for ma <- ma; mb <- f(ma) yield mb

/*
      sequence: flip G[F[A]] ⇒ F[G[A]] using the fold
Input:
  gfa : G[F[A]]

Right-fold via foldRightMStackSafe with:
  z    = empty[G[A]]
  step : (fa: F[A], ga: G[A]) => F[G[A]]
       = map(fa)(a => cons(a, ga))

Flow:
G[F[A]]
   │ (iterate G via Stepper)
   ▼
[fa0, fa1, ..., fa(n-1)]
   │ (right-to-left)
   ▼
acc0 = unit(empty[G[A]]) : F[G[A]]
acc(i+1) = flatMap(acc(i)) { ga =>
            map(fa_i) { a => cons(a, ga) }
          }

Output:
  F[G[A]]

Type flip:
  G[F[A]]  ──sequence──►  F[G[A]]
* */
object SequenceTraverse:
  def map[F[_], A, B](ma: F[A])(f: A => B)(using st: SequenceTraverse[F]): F[B] =
    st.flatMap(ma)(a => st.unit(f(a)))

  def map2[F[_], A, B, C](ma: F[A], mb: F[B])(f: (A, B) => C)(using st: SequenceTraverse[F]): F[C] =
    st.flatMap(ma)(a => map(mb)(b => f(a, b)))

//  sequence: flip G[F[A]] ⇒ F[G[A]] using the fold
  def sequence[F[_], G[_], A](gfa: G[F[A]])(using S: SequenceTraverse[F], Gs: Stepper[G], C: ConsK[G]): F[G[A]] =
    foldRightMStackSafe[G, F, F[A], G[A]](gfa)(C.empty[A]) { (fa, acc) =>
      S.map(fa)(a => C.cons(a, acc))
    }


//     traverse: List[A] -> (A => F[B]) -> F[List[B]]
//    map then fip
    /*
    G = List,  F = Vector,  A = Int
    Input:
      List(Vector(1,2), Vector(3,4)) : G[F[A]]

    Fold right-to-left:

    acc0 = Vector(List()) : F[G[A]]

    fa = Vector(3,4):
      acc1 = flatMap(Vector(List())) { b =>
               map(Vector(3,4)) { a => a :: b }
             }
           = Vector(List(3), List(4))

    fa = Vector(1,2):
      acc2 = flatMap(Vector(List(3), List(4))) { b =>
               map(Vector(1,2)) { a => a :: b }
             }
           = Vector(List(1,3), List(2,3), List(1,4), List(2,4))

    Result:
      Vector[List[Int]] : F[G[A]]

    * */
  def traverse[F[_], G[_], A, B](ga: G[A])(f: A => F[B])
                                 (using S: SequenceTraverse[F], Gs: Stepper[G], C: ConsK[G]): F[G[B]] =
    foldRightMStackSafe[G, F, A, G[B]](ga)(C.empty[B]) { (a, acc) =>
      S.map(f(a))(b => C.cons(b, acc))
    }

/*
  Short version:

* sequence flips structure It turns a container of effects into an effect that yields a container.

  // flip G[F[A]] -> F[G[A]]
  def sequence[F[_], G[_], A](gfa: G[F[A]]): F[G[A]]

* traverse maps, then flips It first applies an effectful function to each element, producing `G[F[B]]`, then calls `sequence` to flip.

  // map A -> F[B], then flip: G[A] -> G[F[B]] -> F[G[B]]
  def traverse[F[_], G[_], A, B](ga: G[A])(f: A => F[B]): F[G[B]]

Key identities (types line up with suitable instantiations):

* sequence(gfa) == traverse(gfa)((fa: F[A]) => fa)
* traverse(ga)(f) == sequence( /* G-map of ga with f */ )
  (Conceptually “map then sequence”; in your setup we rebuilt that `G-map` via `Stepper`+`ConsK`.)

Behavioral notes:

* Order is preserved (your `sequence` folds right-to-left with `cons`).
* Short-circuiting / combination follows `F`.** With your `SequenceTraverse` (which defines `map2` via `flatMap`), effects run left-to-right and fail fast for things like `Option`/`Either`.
* Stack-safe: Your `sequence` is implemented with `foldRightMStackSafe`, so both `sequence` and any `traverse` built on it handle large `G` without blowing the stack.

// F = Option, G = List
sequence(List(Some(1), Some(2)))          // Some(List(1,2))
sequence(List(Some(1), None, Some(3)))    // None

traverse(List(1,2,3))(a => Option(a).filter(_ % 2 == 1))
// None (since 2 maps to None)

Mental model:

* Use sequence when you already have G[F[A]] and you just need to flip.
* Use traverse when you start from G[A] and need to apply an A => F[B] to every element, then flip.

* */
  @main def runSequenceTraverse(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/DesignPatterns/SequenceTraverse.scala created at time 10:14AM")
    import GivenInstances.given
    val lst = List(Vector(1, 2), Vector(3, 4))
    val res1 = sequence(lst)
    val res2 = traverse(List(1, 2, 3))(a => Vector(a, a + 10))
    println(lst)
    println(s"sequence: $res1")
    println(s"traverse: $res2")
