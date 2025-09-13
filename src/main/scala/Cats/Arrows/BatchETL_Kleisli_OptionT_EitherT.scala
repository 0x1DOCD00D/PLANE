
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.Arrows

package Cats.Transformers

import cats.*
import cats.data.*
import cats.implicits.*

object BatchETL_Kleisli_OptionT_EitherT:

  // Base "effect" is Id to keep everything pure and visible;
  // you can replace Id with IO/Future without changing composition.
  type F[A] = Id[A]

  sealed trait Err; case object BadRow extends Err; case object RuleViolated extends Err
  final case class Row(raw: String); final case class Rec(n: Int); final case class Out(n: Int)

  // Step 1: parse. Might fail validation => Either
  val parse: Kleisli[[X]=>>Either[Err, X], Row, Rec] =
    Kleisli { r => Either.catchOnly[NumberFormatException](r.raw.toInt).leftMap(_ => BadRow).map(Rec.apply) }

  // Step 2: filter. Might drop the record => Option
  val filter: Kleisli[Option, Rec, Rec] =
    Kleisli { rec => Option.when(rec.n % 2 == 0)(rec) }

  // Step 3: transform. Might succeed or fail => Either
  val transform: Kleisli[[X]=>>Either[Err, X], Rec, Out] =
    Kleisli { rec => Either.cond(rec.n <= 100, Out(rec.n * 2), RuleViolated) }

  // Combine by picking a uniform carrier: Either first, then add OptionT
  // Behind the scenes:
  //   OptionT[Either[Err,*], A] lets us treat "drop" (None) AND "error" (Left) with the same for-comprehension.
  type Stack[A] = OptionT[[P]=>>Either[Err, P], A]

  def step(row: Row): Stack[Out] =
    for
      rec  <- OptionT.liftF(parse.run(row))      // Either -> Stack
      kept <- OptionT.fromOption[[R]=>>Either[Err, R]](filter.run(rec)) // Option -> Stack
      out  <- OptionT.liftF(transform.run(kept)) // Either -> Stack
    yield out

  @main def runETL(): Unit =
    println(step(Row("42")).value)   // Right(Some(Out(84)))
    println(step(Row("not-int")).value) // Left(BadRow)
    println(step(Row("101")).value)  // Left(RuleViolated)
    println(step(Row("7")).value)    // Right(None)  (dropped by filter)
