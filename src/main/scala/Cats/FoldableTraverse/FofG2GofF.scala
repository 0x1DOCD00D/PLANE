
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.FoldableTraverse

object FofG2GofF:

  import cats.implicits.*

  /*
    def sequence[A](fas: List[F[A]]): F[List[A]]
      traverse(fas)(fa => fa)
 
    def traverse[A, B](as: G[A])(f: A => F[B]): F[G[B]]
      as.foldRight(unit(List[B]()))((a, acc) => f(a).map2(acc)(_ :: _))
  * 
  * */
  val ys: List[Either[String, Int]] = List(Right(1), Left("bad"), Right(3))
  val zs: List[Either[String, Int]] = List(Right(1), Right(2), Right(3))
  val combined1: Either[String, List[Int]] = ys.sequence
  val combined2: Either[String, List[Int]] = zs.sequence

  @main def runFofG2GofF(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Cats/FoldableTraverse/FofG2GofF.scala created at time 7:23PM")
    println(combined1)
    println(combined2)
