
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.Transformers

import cats.data.{OptionT, Writer}

object Transformers:
  import cats.data.{EitherT, OptionT}
  import cats.instances.future.*
  import cats.instances.vector.*
  import cats.syntax.applicative.*

  import scala.concurrent.Future

  //from chapter 5 of the cats textbook
  type FutureEither[A] = EitherT[Future, String, A]//=> Future[Either[String, A]]
  type FutureEitherOption[A] = OptionT[FutureEither, A]
  import cats.data.Writer
  import cats.instances.future.*

  import scala.concurrent.ExecutionContext.Implicits.global

  type Logged[A] = Writer[List[String], A]

  val futureEitherOr: FutureEitherOption[(Int,Int)] =
    for {
      a <- 10.pure[FutureEitherOption]
      b <- 32.pure[FutureEitherOption]
    } yield (a,b)

  def parseNumber(str: String): Logged[Option[Int]] =//Writer[List[String], Option[Int]]
    util.Try(str.toInt).toOption match {
      case Some(num) => Writer(List(s"Read $str"), Some(num))
      case None => Writer(List(s"Failed on $str"), None)
    }

  def addAll(a: String, b: String, c: String): Logged[Option[Int]] =//Writer[List[String], Option[Int]]
  {
    val result = for {
      a <- OptionT(parseNumber(a))
      b <- OptionT(parseNumber(b))
      c <- OptionT(parseNumber(c))
    } yield a + b + c
    result.value
  }

  @main def runMain_Transformers(): Unit =
    val vectorOfOptionsOfInts: OptionT[Vector, Int] = OptionT(Vector(Option(1), Option(2)))//Vector[Option[Int]]
    val res = vectorOfOptionsOfInts.map(x=>{
        println(x)
        x
      })
      println(res)
      println(vectorOfOptionsOfInts)
      println(futureEitherOr)
      val r = addAll("1", "5", "what the hell is that?")
      println(r)
      val r1 = addAll("1", "5", "10")
      println(r1)
