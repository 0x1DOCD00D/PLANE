////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats

import cats.data.Validated.Invalid

object SemigroupCases {

  import cats.*, cats.syntax.all.*
  import cats.data.NonEmptyList

  val xs: NonEmptyList[Int] = NonEmptyList.of(1, 2, 3)
  val sum: Int = xs.reduce // uses Semigroup[Int]

  import cats.syntax.foldable.* // combineAllOption

  val r1: Option[Int] = List(1, 2, 3).combineAllOption // Some(6)
  List.empty[Int].combineAllOption // None

  def combineChunks[A: Semigroup](chunks: List[List[A]]): Option[A] =
    chunks.flatMap(_.reduceOption(_ |+| _)) // partials per chunk
      .reduceOption(_ |+| _) // merge partials

  import cats.*, cats.syntax.all.*
  import cats.instances.map.*, cats.instances.int.*

  val m1 = Map("a" -> 1, "b" -> 2)
  val m2 = Map("b" -> 3, "c" -> 1)
  val merged: Map[String, Int] = m1 |+| m2 // Map(a -> 1, b -> 5, c -> 1)

  import cats.data.*, cats.syntax.all.*

  def pos(i: Int): ValidatedNel[String, Int] =
    if i > 0 then i.validNel else "must be > 0".invalidNel

  def even(i: Int): ValidatedNel[String, Int] =
    if i % 2 == 0 then i.validNel else "must be even".invalidNel

  val lst1: ValidatedNel[String, Unit] = (pos(-2), even(-2)).mapN((_, _) => ())
  val lst2: ValidatedNel[String, List[Int]] = (pos(2), even(2)).mapN((a, b) => List(a, b))

  final case class Conf(host: Option[String], port: Option[Int])

  given Semigroup[Conf] with {
    override def combine(x: Conf, y: Conf): Conf = Conf(
       host = y.host.orElse(x.host), // last defined wins
       port = y.port.orElse(x.port)
    )
  }

  val base = Conf(Some("localhost"), None)
  val env = Conf(None, Some(8080))
  val cli = Conf(Some("example.com"), None)

  val effective: Conf = base |+| env |+| cli // host=Some("example.com"), port=Some(8080)

  @main def runSemigroupCases(args: String*): Unit = {
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Cats/SemigroupCases.scala created at time 3:08PM")
    println(sum)
    println(r1)
    println {
      combineChunks(List(List(1, 2), List(3, 4), List(5))) // Some(15)
    }
    println(merged)
    println {
      effective
    }
    println(lst1)
    println(lst2)
    println {
      pos(2) |+| even(2) // Valid(2)
    }
  }
}
