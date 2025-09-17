
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.FoldableTraverse

object CustomFoldable:
  import cats.*
  import cats.implicits.*

  sealed trait FS[+A]
  final case class Dir[A](name: String, kids: List[FS[A]]) extends FS[A]
  final case class File[A](name: String, a: A) extends FS[A]

  given fsFoldable: Foldable[FS] = new Foldable[FS] {
    def foldLeft[A,B](fa: FS[A], b: B)(f:(B,A)=>B): B = fa match {
      case File(_, sz)     => f(b, sz)
      case Dir(_, children)=> children.foldLeft(b)((acc, c) => foldLeft(c, acc)(f))
    }
    def foldRight[A,B](fa: FS[A], lb: Eval[B])(f:(A,Eval[B])=>Eval[B]): Eval[B] = fa match {
      case File(_, sz)     => f(sz, lb)
      case Dir(_, kids)    => Foldable[List].foldRight(kids, lb)((c, e) => foldRight[A,B](c, e)(f))
    }
  }

  val tree: FS[Long] = Dir("root", List(File("a.txt", 10), Dir("docs", List(File("b.pdf", 32)))))
  /*
    foldMap maps each element to a monoid and then combines them. With identity, 
    it maps each file size to itself, so you just get a monoidal sum of all Longs in the tree.
    def foldMap[A, B](fa: FS[A])(f: A => B)(using M: Monoid[B]): B = foldLeft(fa, M.empty)((b, a) => M.combine(b, f(a)))
    fa is tree: FS[Long].
    f is identity[Long], so A = Long, B = Long.
    Cats provides given Monoid[Long] with empty = 0L and combine = _ + _.
  * */
  val totalBytes: Long = Foldable[FS].foldMap(tree)(identity)
  val fileCount: Int = Foldable[FS].foldMap(tree)(_ => 1) // 2
  val anyLarge: Boolean = Foldable[FS].exists(tree)(_ > 30L) // true
  val firstLarge: Option[Long] = Foldable[FS].find(tree)(_ >= 30L) // Some(32)

  final case class Stats(sum: Long, count: Long)
  given Monoid[Stats] with
    def empty = Stats(0, 0)
    def combine(x: Stats, y: Stats) = Stats(x.sum + y.sum, x.count + y.count)

  val s: Stats = Foldable[FS].foldMap(tree)(bytes => Stats(bytes, 1))
  val avg: Double = if s.count == 0 then 0.0 else s.sum.toDouble
  
  @main def runCustomFoldable(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Cats/FoldableTraverse/CustomFoldable.scala created at time 1:20PM")
    println(s"Total bytes: $totalBytes, file count: $fileCount, anyLarge: $anyLarge, firstLarge: $firstLarge, avg: $avg")
