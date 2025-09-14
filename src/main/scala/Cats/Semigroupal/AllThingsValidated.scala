
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.Semigroupal

import cats.syntax.all.*

object AllThingsValidated:

  import cats.data.{Validated, ValidatedNel, NonEmptyList}

  final case class SignUp(name: String, age: Int, email: String, country: String, newsletter: Boolean)

  type V[A] = ValidatedNel[String, A]

  def get(m: Map[String, String], key: String): V[String] =
    m.get(key).filter(_.nonEmpty).toValidNel(s"Missing or blank '$key'")

  def parseInt(s: String, key: String): V[Int] =
    Either
      .catchOnly[NumberFormatException](s.trim.toInt)
      .leftMap(_ => s"'$key' must be an integer: '$s'")
      .toValidatedNel

  def within(min: Int, max: Int)(n: Int): V[Int] =
    Validated.condNel(n >= min && n <= max, n, s"age must be in [$min,$max]")

  def parseBool(s: String, key: String): V[Boolean] =
    s.trim.toLowerCase match {
      case "true" | "t" | "1" => true.validNel
      case "false" | "f" | "0" => false.validNel
      case other => s"'$key' must be boolean (true/false/1/0), was '$other'".invalidNel
    }

  private val EmailRx = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$".r

  def emailFormat(s: String): V[String] =
    Validated.condNel(EmailRx.matches(s), s, s"email invalid: $s")

  def validate(m: Map[String, String]): V[SignUp] = {
    (
      get(m, "name"),
      get(m, "age").andThen(parseInt(_, "age")).andThen(within(13, 120)),
      get(m, "email").andThen(emailFormat),
      get(m, "country"),
      get(m, "newsletter").andThen(parseBool(_, "newsletter"))
    ).mapN(SignUp.apply) // Semigroupal + Functor under the hood
  }

  @main def runAllThingsValidated(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Cats/Semigroupal/AllThingsValidated.scala created at time 12:43PM")
    val bad = Map("name" -> "", "age" -> "abc", "email" -> "nope", "country" -> "", "newsletter" -> "maybe")
    val result = validate(bad)
    println(result)
// => Invalid(NonEmptyList(Missing or blank 'name', 'age' must be an integer: 'abc', email invalid: nope, Missing or blank 'country', 'newsletter' must be boolean (true/false/1/0), was 'maybe'))

