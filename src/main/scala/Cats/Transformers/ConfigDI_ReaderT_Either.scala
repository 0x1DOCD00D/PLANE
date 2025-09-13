
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.Transformers

import cats.*
import cats.data.*
import cats.implicits.*

object ConfigDI_ReaderT_Either:

  final case class Cfg(host: String, port: Int)
  sealed trait ConfigError; 
  case object HostMissing extends ConfigError; 
  case object PortBad extends ConfigError

  type App[A] = ReaderT[[Z]=>>Either[ConfigError, Z], Cfg, A]
  // Behind the scenes:
  //   ReaderT[F, R, A] == R => F[A]; here F == Either[ConfigError, *], R == Cfg.
  //   So App[A] is a function Cfg => Either[ConfigError, A] with flatMap that threads the same Cfg.

  val checkHost: App[String] =
    ReaderT { cfg => Either.cond(cfg.host.nonEmpty, cfg.host, HostMissing) }

  val checkPort: App[Int] =
    ReaderT { cfg => Either.cond(cfg.port > 0 && cfg.port < 65536, cfg.port, PortBad) }

  val buildUrl: App[String] =
    for
      h <- checkHost
      p <- checkPort
    yield s"http://$h:$p"

  @main def runReader(): Unit =
    println(buildUrl.run(Cfg("localhost", 8080))) // Right(http://localhost:8080)
    println(buildUrl.run(Cfg("", 8080)))          // Left(HostMissing)
