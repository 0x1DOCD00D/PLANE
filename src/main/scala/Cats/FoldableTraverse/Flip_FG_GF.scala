
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.FoldableTraverse

object Flip_FG_GF:
  import scala.concurrent.*
  import scala.concurrent.duration.*
  import ExecutionContext.Implicits.global
  import cats.* 
  import cats.implicits.*

  type SKU = String

  def fetchPrice(sku: SKU): Future[BigDecimal] =
    Future {
      /* HTTP/db work */ BigDecimal(sku.length * 1.99)
    }

  val skus = List("A12", "B0009", "C-XYZ")

    // Future[List[BigDecimal]] = List[Future[BigDecimal]].traverse
  /*
    traverse takes a structure `G[A]` and a function `A => F[B]`, and returns `F[G[B]]`.
    Type: `traverse[G[_], F[_], A, B](ga: G[A])(f: A => F[B]): F[G[B]]`
    Intuition: map each element into an effect, then “flip” so you get one effect of the whole structure.
  
    def sequence[A](fas: List[F[A]]): F[List[A]] = traverse(fas)(fa => fa)
    def traverse[A, B](as: List[A])(f: A => F[B]): F[List[B]] = as.foldRight(unit(List[B]()))((a, acc) => f(a).map2(acc)(_ :: _))
    extension [A](fa: F[A])
     def map2[B, C](fb: F[B])(f: (A, B) => C): F[C] = a.flatMap(a => fb.map(b => f(a, b)))   

    sequence is the specialization that actually flips a nested type
    From `G[F[A]]` to `F[G[A]]`.
    Type: `sequence[G[_], F[_], A](gfa: G[F[A]]): F[G[A]]`
    And `traverse(ga)(f)` ≡ `sequence(ga.map(f))`.

    So, the correct “flip” is G[F[_]] → F[G[_]] (via `sequence`). Your `List`/`Future` example is exactly this List[Future[Price]] → Future[List[Price]]
  
    traverse[G[_]: Traverse, F[_]: Applicative, A, B](ga: G[A])(f: A => F[B]): F[G[B]]
    G = List
    F = Future
    A = SKU
    B = BigDecimal
    
    ga.traverse(f) ≡ ga.map(f).sequence
    For List, sequence turns List[Future[B]] into Future[List[B]]
    def traverseList[A, B](as: List[A])(f: A => Future[B]): Future[List[B]] =
      as.foldRight(Future.successful(List.empty[B])) { (a, accF) =>
        (f(a), accF).mapN(_ :: _)     // mapN combines the latest Future with the accumulated Future
    }
  * */
  val pricesF: Future[List[BigDecimal]] = skus.traverse(fetchPrice) // Future[List[BigDecimal]]

  val totalF: Future[BigDecimal] = pricesF.map(_.sum)

  @main def runFlip_FG_GF(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Cats/FoldableTraverse/Flip_FG_GF.scala created at time 2:17PM")
    println(s"Total price: ${Await.result(totalF, 2.seconds)}")
