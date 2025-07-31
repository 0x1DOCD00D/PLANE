////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scala3Features.MatchTypes

//https://users.scala-lang.org/t/combine-match-types-and-either-a-match-type-could-not-be-fully-reduced/9445/2
//https://dotty.epfl.ch/docs/reference/new-types/match-types.html#dependent-typing

object CombineWithEither {
  type B[X <: Boolean] = X match {
    case true  => Option[String]
    case false => String
  }

  class C(val b: Boolean, val c: B[b.type])

  def bWitness(b: Boolean, s: Option[String]): B[b.type] = b match {
    case _: true  => s
    case _: false => s.getOrElse("")
  }

  def buildC(myBool: Boolean, maybeString: Option[String]): C = C(myBool, bWitness(myBool, maybeString))

  case class Error(msg: String)

  /*
  def bWitnessEither(b: Boolean, s: Option[String]): Either[Error, B[b.type]] = b match {
    case _: true => Right(s)
    case _: false => s.map(Right(_)).getOrElse(Left(Error("Not value present.")))
  }


  def bWitnessEither2(b: Boolean, s: Option[String]): Either[Error, B[b.type]] = b match {
    case _: true => Right(s) // The compiler seems to only complain in this case
    case _: false => Left(Error("foo"))
  }
   */

//  The problem here is that this dependent typing only works when the result type is a match type,
  //  so wrapping it in Either breaks the special case. Instead define a new match type

  type EitherB[X <: Boolean] = X match {
    case true  => Either[Error, Option[String]]
    case false => Either[Error, String]
  }

  def bWitnessEither(b: Boolean, s: Option[String]): EitherB[b.type] = b match {
    case _: true  => Right(s)
    case _: false => s.map(Right(_)).getOrElse(Left(Error("Not value present.")))
  }

  def main(args: Array[String]): Unit = {
    val c1 = C(true, None)
    val c2 = C(true, Some("Foo"))
    val c3 = C(false, "Bar")
//    val c4 = C(false, Some("Foo")) //      Does not compile as expected
//    val c5 = C(true, "Foo")			//	Does not compile as expected

  }
}
