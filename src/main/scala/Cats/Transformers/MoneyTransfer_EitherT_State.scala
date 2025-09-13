
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.Transformers
import cats.*
import cats.data.*
import cats.implicits.*

object MoneyTransfer_EitherT_State:
  type Account = String
  type Ledger  = Map[Account, BigDecimal]
  sealed trait ErrorAccount
  case object NoFunds extends ErrorAccount; 
  case object NoAccount extends ErrorAccount

  // Helpers
  def getBalance(acc: Account): State[Ledger, Option[BigDecimal]] =
    State.inspect(_.get(acc)) // S => (S, Option[BigDecimal])

  def putBalance(acc: Account, amount: BigDecimal): State[Ledger, Unit] =
    State.modify(_.updated(acc, amount))

  // Transformer stack alias for readability
  type Tx[A] = EitherT[[X]=>>State[Ledger, X], ErrorAccount, A]

  // Behind the scenes:
  //   EitherT[F, E, A] wraps an F[Either[E, A]] and gives us flatMap that short-circuits on Left.
  //   Here F == State[Ledger, *], so Tx[A] == State[Ledger, Either[ErrorAccount, A]] with nice methods.

  def transfer(from: Account, to: Account, amt: BigDecimal): Tx[Unit] =
    for
      balFrom <- EitherT.fromOptionF(getBalance(from), NoAccount)
      balTo   <- EitherT.fromOptionF(getBalance(to),   NoAccount)
      _       <- EitherT.cond[[Y]=>>State[Ledger, Y]](balFrom >= amt, (), NoFunds)
      _       <- EitherT.liftF(putBalance(from, balFrom - amt))
      _       <- EitherT.liftF(putBalance(to,   balTo   + amt))
    yield ()

  @main def runTransfers(): Unit =
    val start: Ledger = Map("alice" -> 50, "bob" -> 20)
    val (end1, res1)  = transfer("alice", "bob", 25).value.run(start).value
    // res1: Either[ErrorAccount, Unit], end1: Ledger
    println(res1 -> end1) // Right(()) -> Map(alice -> 25, bob -> 45)

    val (end2, res2)  = transfer("alice", "bob", 40).value.run(end1).value
    println(res2 -> end2) // Left(NoFunds) -> unchanged ledger
