
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.Transformers

object TinyReaderTExample:
  import cats.data.ReaderT
  import cats.implicits.*
  /*
    ReaderT[[W] =>> Either[Err, W], Cfg, A] means “a function that takes a Cfg and returns an Either[Err, A],”
    packaged with map/flatMap that automatically threads the same Cfg through the whole computation.

    ReaderT[F, R, A] is just R => F[A].

    Here F[X] = Either[Err, X] (that type lambda [[W] =>> Either[Err, W]] is the Scala 3 way to say Either[Err, *]).

    R = Cfg, A is your result type.

    So the underlying type is Cfg => Either[Err, A].

    Why it’s useful.

    You can write business logic that needs a read-only environment (Cfg) and may fail with a domain error (Err), while keeping code linear with for-comprehensions.

    pure(a) becomes cfg => Right(a).

    flatMap runs both steps with the same cfg, and short-circuits if the first step returns Left(err).
  *
  * */

  final case class Cfg(host: String, port: Int)
  sealed trait Err;
  case object BadHost extends Err;
  case object BadPort extends Err

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

  @main def runTinyReaderTExample(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Cats/Transformers/TinyReaderTExample.scala created at time 9:22AM")
    // Running it
    url.run(Cfg("localhost", 8080)) // Right("http://localhost:8080")
    url.run(Cfg("", 8080)) // Left(BadHost)

