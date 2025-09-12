
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.Applicatives

/*
* don’t make the case class JobRecordF itself an Applicative (it’s a concrete value type, kind *).
* Instead use Applicative instances for effect/container types like Option, ValidatedNel, IO, etc.
* to build JobRecordF from F[String] and F[Int]. That gives you generic, composable code that
* (depending on F) can accumulate errors, run effects in parallel, or keep things simple and short-circuiting.
* */
import cats.Applicative
import cats.data.{Validated, ValidatedNel}
import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.syntax.all.*

import scala.concurrent.duration.*

case class JobRecord(id: String, years: Int)

object JobRecord:
  def buildJobRecord[F[_]: Applicative](idF: F[String], yearsF: F[Int]): F[JobRecord] =
    (idF, yearsF).mapN(JobRecord.apply)   // mapN is Applicative-based

val oId: Option[String]   = Some("job-42")
val oYears: Option[Int]   = Some(5)
val jobOpt: Option[JobRecord] = JobRecord.buildJobRecord(oId, oYears)

val jobOptMissing = JobRecord.buildJobRecord(Option.empty[String], oYears)

type V[A] = ValidatedNel[String, A]

def validateId(s: String): V[String] = if s.nonEmpty then s.validNel else "id empty".invalidNel

def validateYears(n: Int): V[Int] = if n >= 0 then n.validNel else "years negative".invalidNel
val app = summon[Applicative[V]] // summons Applicative[[α] =>> Validated[String, α]]

val vGood: V[JobRecord] = JobRecord.buildJobRecord(validateId("r1"), validateYears(3))
val vBad: V[JobRecord] = JobRecord.buildJobRecord(validateId(""), validateYears(-1))
// Invalid(NonEmptyList("id empty", "years negative"))  <-- both errors accumulated

val ioId: IO[String] = IO.sleep(500.millis) *> IO.pure("job-async")
val ioYears: IO[Int] = IO.sleep(500.millis) *> IO.pure(10)

// sequential (mapN on IO is left-to-right sequential)
val ioSeq: IO[JobRecord] = JobRecord.buildJobRecord(ioId, ioYears) // ~1s because both sleep sequentially

// parallel via Parallel/IO.parMapN
val ioPar: IO[JobRecord] = (ioId, ioYears).parMapN(JobRecord.apply) // ~0.5s

// run them just to demonstrate (unsafe for REPL demo only)
@main def demoApplicativeJobRecord(): Unit =
  println(jobOpt)
  println(jobOptMissing)
  println(vGood)
  println(vBad)
  println("Running sequential IO...")
  println(ioSeq.unsafeRunSync())   // slower
  println("Running parallel IO...")
  println(ioPar.unsafeRunSync())   // faster
