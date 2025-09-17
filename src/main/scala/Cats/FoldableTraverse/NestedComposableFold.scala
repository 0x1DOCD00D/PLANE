
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.FoldableTraverse

import cats.implicits.catsSyntaxEq

object NestedComposableFold:
  import cats.Foldable

  final case class Story(points: Int)

  /*
    The container is nested: F[G[Story]] where F = List and G = Vector. We want to “sum over everything inside” without writing two loops.
    Outer shape: List[ ... ]
    Inner shape: Vector[Story]
    Payload: Story(points: Int)
  * */
  val backlog: List[Vector[Story]] = List(Vector(Story(3), Story(5)), Vector(Story(8)))

  /*
   Cats lets you compose two Foldables into one that knows how to fold the nested shape.
   If F[_] and G[_] are Foldable, then F compose G is a Foldable for H[X] = F[G[X]].
   type H[X] = List[Vector[X]]
   val H: Foldable[H] = Foldable[List].compose[Vector]

    foldMap maps each element to a Monoid and combines them. For Int, Cats’ Monoid[Int] uses addition with 0.

    Foldable[List].foldMap(backlog) { vec =>
      Foldable[Vector].foldMap(vec)(st => st.points)
    }

  Why not backlog.flatten.map(_.points).sum?
  That works, but has drawbacks
    Allocations: flatten builds an intermediate collection.
    Generality: with Foldable you can swap Vector for NonEmptyList, Chain, custom trees, etc., and the same code keeps working.
    Laws & reuse: you get all Foldable combinators for free on the composed structure.
  * */
  val points: Int = (Foldable[List] compose Foldable[Vector]).foldMap(backlog)(_.points) // 16

  def totalPoints[F[_] : Foldable, G[_] : Foldable](xs: F[G[Story]]): Int =
    Foldable[F].compose[G].foldMap(xs)(_.points)
  val total = totalPoints(backlog)

  import cats._, cats.implicits._

  final case class Stats(sum: Int, count: Int)

  given Monoid[Stats] with
    def empty = Stats(0, 0)
    def combine(a: Stats, b: Stats) = Stats(a.sum + b.sum, a.count + b.count)

  def stats[F[_] : Foldable, G[_] : Foldable](xs: F[G[Story]]): Stats =
    Foldable[F].compose[G].foldMap(xs)(s => Stats(s.points, 1))

  val s = stats(backlog) // Stats(16, 3)
  val avg = if s.count == 0 then 0.0 else s.sum.toDouble / s.count

  type H3[X] = List[Vector[Option[X]]]
  val H3F: Foldable[H3] =
    Foldable[List].compose[Vector].compose[Option]

  val xs: H3[Story] = List(Vector(Some(Story(2)), None), Vector(Some(Story(4))))
  val sum3 = H3F.foldMap(xs)(_.points) // 6


  @main def runNestedComposableFold(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Cats/FoldableTraverse/NestedComposableFold.scala created at time 12:31PM")
    println(s"Points: $points")
    println(s"Total: $total")
    println(points === total)
    println(s"Stats: $s")
    println(s"Avg: $avg")
    println(points === s.sum)
    println(s"Sum3: $sum3")
    println(sum3 === s.sum)
