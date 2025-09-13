
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.Transformers

package Cats.Transformers

import cats.*
import cats.data.*
import cats.implicits.*

object GeoService_OptionT_ReaderT_Either:

  // Domain
  final case class Cfg(apiKey: String)
  final case class Location(lat: Double, lon: Double)
  sealed trait Err; 
  case object Unauthorized extends Err; 
  case object BackendDown extends Err

  // Type aliases to reduce noise
  type F[A]   = ReaderT[[W]=>>Either[Err, W], Cfg, A]
  type Geo[A] = OptionT[F, A] // === OptionT[ReaderT[Either[Err, *], Cfg, *], A]

  // Cache (just a Map stub)
  val cache: Map[String, Location] =
    Map("UIC" -> Location(41.87, -87.65))

  def fromCache(name: String): F[Option[Location]] =
    ReaderT.pure(cache.get(name)) // pure lifts to Right(Some/None) and ignores config

  def remoteFetch(name: String): F[Option[Location]] =
    ReaderT { cfg =>
      if cfg.apiKey.isEmpty then Left(Unauthorized)
      else if name == "down" then Left(BackendDown)
      else Right(Some(Location(47.61, -122.33))) // pretend Seattle
    }

  // Behind the scenes:
  //   OptionT composes the behaviors: fail with Either[Err,*] (outer), and try cache/remote (inner Option).
  //   .value returns ReaderT[Either[Err,*], Cfg, Option[Location]], which we can .run with a Cfg and unwrap Either.

  def lookup(name: String): Geo[Location] =
    OptionT(fromCache(name)) <+> OptionT(remoteFetch(name)) // try cache first, else remote

  @main def runGeo(): Unit =
    val cfg = Cfg(apiKey = "secret")
    println( lookup("UIC").value.run(cfg) )     // Right(Some(Location(...)))
    println( lookup("unknown").value.run(cfg) ) // Right(Some(Location(...))) via remote
    println( lookup("down").value.run(cfg) )    // Left(BackendDown)

