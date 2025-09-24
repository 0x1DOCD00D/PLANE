
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.Transformers

object LookupXformer:
  import cats.*
  import cats.data.*
  import cats.implicits.*

  final case class UserId(value: Long) extends AnyVal
  final case class User(id: UserId, name: String)
  sealed trait ErrorCode;
  case object DbDown extends ErrorCode;
  case object BadId extends ErrorCode

  // Simulated cache & db
  val cache: Map[UserId, User] = Map(UserId(1) -> User(UserId(1), "Ada"))

  def dbFetch(id: UserId): Either[ErrorCode, Option[User]] =
    // pretend: DB is up and returns either a user row or none
    Right(Some(User(id, s"user-${id.value}"))).filterOrElse(
      _.exists(_.id.value != 666),
      DbDown
    )

  // -------------- Core idea: lift Either[ErrorCode, Option[User]] into OptionT[Either[ErrorCode, *], User]
  // Behind the scenes:
  //   OptionT[F, A] == { value: F[Option[A]] }
  //   Here F == Either[ErrorCode, *].
  //   - OptionT(dbFetch(id)).map(...) applies the map only if Option is Some and Either is Right.
  //   - .value unwraps back to Either[ErrorCode, Option[A]] when you need the base type again.

  def cached(id: UserId): Either[ErrorCode, Option[User]] =
    Right(cache.get(id)) // cache miss -> None, a -> Some(u)

  def findUser(id: Long): Either[ErrorCode, Option[User]] =
    for
      normalized <- Either.cond(id > 0, UserId(id), BadId) // Either[ErrorCode, UserId]
      // Build a small OptionT program that tries cache first, or else DB
      result <- (
        /*
          turns an Either[ErrorAccount, Option[User]] into an OptionT[Either[ErrorAccount, *], User] so you can work as if
          you had a plain User while still carrying both “may be missing” and “may error” through your code.
          def cached(id: UserId): Either[ErrorAccount, Option[User]] = Right(cache.get(id))
          val eou: Either[ErrorAccount, Option[User]] = cached(normalized)
          val ot : OptionT[Either[ErrorAccount, *], User] = OptionT(eou)
          OptionT is a tiny wrapper around an F[Option[A]]. Here F is Either[ErrorAccount, *] and A is User, so
          OptionT(cached(normalized)) is just new OptionT[Either[ErrorAccount, *], User](value = cached(normalized))

          In Either[ErrorAccount, *] (and friends) the * is a type hole: it marks the spot for the yet-unknown type argument
          so you can talk about a type constructor rather than a fully applied type.
          Think of it as “the function on types”
          Either[ErrorAccount, *] means the unary type constructor A ↦ Either[ErrorAccount, A].
          OptionT[Either[ErrorAccount, *], User] ≡ OptionT[[A] =>> Either[ErrorAccount, A], User].
          EitherT[State[Ledger, *], ErrorAccount, A] ≡ EitherT[[X] =>> State[Ledger, X], ErrorAccount, A].
          If you had two holes, e.g. Kleisli[IO, *, *] ≡ [R, A] =>> Kleisli[IO, R, A].
        * */
        OptionT(cached(normalized)) <+> // .<+> tries left OptionT, if None then tries right
          OptionT(dbFetch(normalized))
        ).value // unwrap back to Either[ErrorCode, Option[User]]
    yield result

  /*
    final case class OptionT[F[_], A](value: F[Option[A]])
    and we solve for F and A given
    val v: Either[ErrorCode, Option[User]]

    Unify value: F[Option[A]] with your v:

    1. Match the inner `A`.
       `Option[A]` must be `Option[User]`, so A = User.

    2. Solve for the outer `F[_]`.
       `F[Option[User]]` must be `Either[ErrorCode, Option[User]]`, so F[X] = Either[ErrorCode, X].

    Writing that `F` in Scala 3:
     As a type lambda: `[[X] =>> Either[ErrorCode, X]]`
     Or (with kind-projector syntax) as `Either[ErrorCode, *]`

    Therefore:
    val ot: OptionT[[X] =>> Either[ErrorCode, X], User] = OptionT(v)

    // equivalently (with * syntax):
    val ot2: OptionT[Either[ErrorCode, *], User] =  OptionT(v)

    And going the other way:

    ot.value  // : Either[ErrorCode, Option[User]]

    We just factored out the pattern `F[Option[A]]` by choosing `A = User` and `F[X] = Either[ErrorCode, X]`.
  * */

  @main def runLookupXformer(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Cats/Transformers/LookupXformer.scala created at time 11:33AM")
    val v = OptionT(Right(Some(User(UserId(1), "Ada")))).value // : Either[ErrorCode, Option[User]] and removing Some will not compile
    println(findUser(1L)) // Right(Some(User(1, "Ada")))
    println(findUser(2L)) // Right(Some(User(2, "user-2")))
    println(findUser(-5L)) // Left(BadId)

