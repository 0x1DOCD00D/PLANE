
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.SemigroupsMonoids

object SemigroupK_v4 {
  import cats.data.{Kleisli, ValidatedNel}
  import cats.syntax.all.*

  type V[A] = ValidatedNel[String, A]
  type Decoder[A] = Kleisli[V, String, A]

  // "42"
  val asInt: Decoder[Int] = Kleisli { s =>
    Either.catchOnly[NumberFormatException](s.toInt)
      .leftMap(_ => s"'$s' is not a decimal int").toValidatedNel
  }

  // "user-42"
  val userId: Decoder[Int] = Kleisli { s =>
    if s.startsWith("user-") then
      Either.catchOnly[NumberFormatException](s.stripPrefix("user-").toInt)
        .leftMap(_ => s"'$s' has non-integer suffix").toValidatedNel
    else s"Missing 'user-' prefix in '$s'".invalidNel
  }

  // Try decimal first, then prefixed. If both fail, errors accumulate.
  val idDecoder: Decoder[Int] = asInt <+> userId

  def main(args: Array[String]): Unit = {
    val ok1 = idDecoder.run("37") // Valid(37)
    val ok2 = idDecoder.run("user-93") // Valid(93)
    val bad = idDecoder.run("abc") // Invalid(NEL("'abc' is not a decimal int", "Missing 'user-' prefix in 'abc'"))

    println(ok1)
    println(ok2)
    println(bad)
  }
}
