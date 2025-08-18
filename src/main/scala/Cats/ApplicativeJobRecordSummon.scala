
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats

object ApplicativeJobRecordSummon {
  import cats.Applicative
  import cats.syntax.all._ // mapN, .ap extension, validNel, invalidNel
  import cats.data.ValidatedNel
  import cats.data.Validated

  case class JobRecord(id: String, years: Int)

  // simple validators returning ValidatedNel[String, T]
  def validateId(s: String): ValidatedNel[String, String] =
    if s.nonEmpty then s.validNel else "id is empty".invalidNel

  def validateYears(n: Int): ValidatedNel[String, Int] =
    if n >= 0 then n.validNel else "years must be >= 0".invalidNel

  @main def validatedAppExample(): Unit =
    type V[A] = ValidatedNel[String, A]

    // summon the Applicative instance for ValidatedNel[String, *]
    val app = summon[Applicative[V]]

    // two example inputs
    val goodId = validateId("job-42") // Valid("job-42")
    val goodYears = validateYears(5) // Valid(5)

    val badId = validateId("") // Invalid(NonEmptyList("id is empty"))
    val badYears = validateYears(-1) // Invalid(NonEmptyList("years must be >= 0"))

    // 1) Using app.pure + ap (explicit function-in-context)
    // note: JobRecord.apply is (String, Int) => JobRecord, so we lift a curried constructor
    val viaApGood: V[JobRecord] =
      app.pure((id: String) => (yrs: Int) => JobRecord(id, yrs)).ap(goodId).ap(goodYears)

    println(s"viaApGood = $viaApGood") // Valid(JobRecord("job-42", 5))

    // If both fields are invalid, errors are accumulated
    val viaApBad: V[JobRecord] =
      app.pure((id: String) => (yrs: Int) => JobRecord(id, yrs)).ap(badId).ap(badYears)

    println(s"viaApBad  = $viaApBad") // Invalid(NonEmptyList("id is empty", "years must be >= 0"))

    // 2) More idiomatic: mapN (same Applicative, nicer syntax)
    val viaMapNGood: V[JobRecord] = (goodId, goodYears).mapN(JobRecord.apply)
    val viaMapNBad: V[JobRecord] = (badId, badYears).mapN(JobRecord.apply)

    println(s"viaMapNGood = $viaMapNGood")
    println(s"viaMapNBad  = $viaMapNBad")

    // 3) Show a mixed case: one valid, one invalid -> single error
    val mixed: V[JobRecord] = (goodId, badYears).mapN(JobRecord.apply)
    println(s"mixed = $mixed")

}
