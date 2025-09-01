
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats

object ApplicativeJobRecord_v2 {
  import cats.Applicative
  import cats.syntax.all._ // mapN, valid/invalid, ap, etc.
  import cats.data.ValidatedNel
  import cats.data.Validated
  import cats.effect.IO
  import cats.effect.unsafe.implicits.global
  import scala.concurrent.duration._

  case class JobRecord(id: String, years: Int)

  // generic builder using Applicative
  def buildJobRecord[F[_] : Applicative](idF: F[String], yearsF: F[Int]): F[JobRecord] =
    (idF, yearsF).mapN(JobRecord.apply) // mapN is Applicative-based

  // 1) Option: simple, short-circuiting
  val oId: Option[String] = Some("job-42")
  val oYears: Option[Int] = Some(5)
  val jobOpt: Option[JobRecord] =
    buildJobRecord(oId, oYears) // Some(JobRecord("job-42", 5))

  val jobOptMissing: Option[JobRecord] = buildJobRecord(Option.empty[String], oYears) // None

  // 2) ValidatedNel: validation with error accumulation
  type V[A] = ValidatedNel[String, A]

  def validateId(s: String): V[String] =
    if s.nonEmpty then s.validNel else "id empty".invalidNel

  def validateYears(n: Int): V[Int] =
    if n >= 0 then n.validNel else "years negative".invalidNel

  val vGood: V[JobRecord] =
    buildJobRecord(validateId("r1"), validateYears(3))
  // Valid(JobRecord("r1", 3))

  val vBad: V[JobRecord] =
    buildJobRecord(validateId(""), validateYears(-1))
  // Invalid(NonEmptyList("id empty", "years negative"))  <-- both errors accumulated

  // 3) IO: independent effects; you can run them sequentially or in parallel
  val ioId: IO[String] = IO.sleep(500.millis) *> IO.pure("job-async")
  val ioYears: IO[Int] = IO.sleep(500.millis) *> IO.pure(10)

  // sequential (mapN on IO is left-to-right sequential)
  val ioSeq: IO[JobRecord] = buildJobRecord(ioId, ioYears) 

  // parallel via Parallel/IO.parMapN
  val ioPar: IO[JobRecord] = (ioId, ioYears).parMapN(JobRecord.apply) 

  // run them just to demonstrate (unsafe for REPL demo only)
  @main def demoV2(): Unit =
    println(jobOpt)
    println(jobOptMissing)
    println(vGood)
    println(vBad)
    println("Running sequential IO...")
    println(ioSeq.unsafeRunSync()) // slower
    println("Running parallel IO...")
    println(ioPar.unsafeRunSync()) // faster
}
