
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats

/*
 *
 *  Copyright (c) 2021. Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *   
 *   Unless required by applicable law or agreed to in writing, software distributed under
 *   the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *   either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *  
 */

import cats.Applicative
import cats.data.Nested
import cats.implicits.*

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import cats.instances.option.*

object Applicatives:
  val optionApplicative: Applicative[Option] = Applicative[Option]
  val f: (Int, Char) => Double = (i, c) => (i + c).toDouble

  val vint: Option[Int] = Some(5)
  val vchar: Option[Char] = Some('a')

  @main def runMain_Applicatives(): Unit =
    val res1: Option[Char => Double] = vint.map(i => (c: Char) => f(i, c))
//    We have an Option[Char => Double] and an Option[Char] to which we want to apply the function to,
//    but map doesn't give us enough power to do that. Hence, ap.
//    https://typelevel.org/cats/typeclasses/applicative.html
    println(s"exp1: ${res1.get.apply(vchar.get)}")

    val res2: Option[Double] = optionApplicative.ap(res1)(vchar)
    println(res2)

    val res3 = vint.map(i=> vchar.map(c=>f(i,c)))
    println(s"exp1: $res3")

    val x: Future[Option[Int]] = Future.successful(Some(5))
    val y: Future[Option[Char]] = Future.successful(Some('a'))
    val composed: Future[Option[Int]] = Applicative[Future].compose[Option].map2(x, y)(_ + _)
    import concurrent.duration.DurationInt
    Await.result(composed, 2.second)
    println(s"exp3: $composed")