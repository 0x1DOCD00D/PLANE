
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

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

  /*
    ReaderT[[W] =>> Either[Err, W], Cfg, A]` means “a function that takes a `Cfg` and returns an `Either[Err, A]`,”
    packaged with `map/flatMap` that automatically threads the same `Cfg` through the whole computation.
    `ReaderT[F, R, A]` is just `R => F[A]`.
    Here `F[X] = Either[Err, X]` (that type lambda `[[W] =>> Either[Err, W]]` is the Scala 3 way to say `Either[Err, *]`).
    `R = Cfg`, `A` is your result type.
    So the underlying type is `Cfg => Either[Err, A]`.

    You can write business logic that needs a read-only environment (`Cfg`) and may fail with a domain error (`Err`), while keeping code linear with `for`-comprehensions.
    `pure(a)` becomes `cfg => Right(a)`.
    `flatMap` runs both steps with the same `cfg`, and short-circuits if the first step returns `Left(err)`.

    import cats.data.ReaderT
    import cats.implicits.*
    final case class Cfg(host: String, port: Int)
    sealed trait Err; case object BadHost extends Err; case object BadPort extends Err

    type App[A] = ReaderT[[X] =>> Either[Err, X], Cfg, A] // == Cfg => Either[Err, A]

    val hostOk: App[String] =
      ReaderT { cfg => Either.cond(cfg.host.nonEmpty, cfg.host, BadHost) }

    val portOk: App[Int] =
      ReaderT { cfg => Either.cond(cfg.port > 0 && cfg.port < 65536, cfg.port, BadPort) }

    val url: App[String] =
      for
        h <- hostOk
        p <- portOk
      yield s"http://$h:$p"

    // Running it
    url.run(Cfg("localhost", 8080)) // Right("http://localhost:8080")
    url.run(Cfg("", 8080))          // Left(BadHost)

    `ReaderT.ask[F, Cfg]` gives you the current `Cfg`.
    `reader.local(f)` runs the reader with a transformed config `f(cfg)`, useful for scoping changes.
    `ReaderT.liftF(e: Either[Err, A])` lifts a plain `Either` into the reader, ignoring `Cfg`.

    Equivalences you might see.

    `ReaderT[[W] =>> Either[Err, W], Cfg, A]` is the same as `Kleisli[Either[Err, *], Cfg, A]`.
    It’s isomorphic to `EitherT[Reader[Cfg, *], Err, A]` because `Reader[Cfg, X]` is `Cfg => X`.
    Use whichever form composes more cleanly with your stack.
  * */

  type F[A]   = ReaderT[[W]=>>Either[Err, W], Cfg, A]
  type Geo[A] = OptionT[F, A] // === OptionT[ReaderT[Either[Err, *], Cfg, *], A]

  // Cache (just a Map stub)
  val cache: Map[String, Location] = Map("UIC" -> Location(41.87, -87.65))

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

