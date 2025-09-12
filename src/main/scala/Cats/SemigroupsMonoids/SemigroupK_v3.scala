
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.SemigroupsMonoids

import cats.data.OptionT
import cats.effect.{IO, IOApp}
import cats.syntax.all.*

import scala.concurrent.duration.*

object SemigroupK_v3 extends IOApp.Simple {

  final case class User(id: String)

  def source(name: String, hit: Boolean, delayMs: Long): OptionT[IO, User] =
    OptionT {
      IO.sleep(delayMs.millis) *>
        IO.println(s"[$name] queried, hit=$hit") *>
        IO.pure(if (hit) Some(User(s"$name-user")) else None)
    }

  // Try primary, then cache, then DB. Only as many calls as needed.
  def fetchUser(id: String): OptionT[IO, User] =
    source("primary", hit = false, delayMs = 300) <+>
      source("cache", hit = true, delayMs = 30) <+>
      source("db", hit = true, delayMs = 80)

  def run: IO[Unit] =
    fetchUser("42").value.flatMap {
      case Some(u) => IO.println(s"Fetched: $u")
      case None => IO.println("Not found anywhere")
    }
}
