////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.ExternalFrameworks

import cats.effect.{ExitCode, IO, IOApp}

import pureconfig.generic.*
import pureconfig.generic.derivation.*
import pureconfig.{ConfigFieldMapping, ConfigReader, ConfigSource}
import CatsIO.Helpers.{blue, bold, green, red}
import cats.MonadThrow
import cats.implicits.catsSyntaxEither
import pureconfig.error.ConfigReaderException
import scala.reflect.ClassTag
import pureconfig.generic.semiauto.deriveReader

/*
in application.conf:
MySql {
  url = "jdbc:mysql://localhost:3306/board"
  user = "root"
  connection-params {
    use-ssl = false
    server-timezone = "UTC"
    allow-public-key-retrieval = true
    connection-pool-size = 10
    connection-timeout = 30000
    idle-timeout = 600000
  }
  driver = "com.mysql.cj.jdbc.Driver"
}* */

object PureconfigBasics extends IOApp:
  def log[T](message: String, instance: T): T =
    println(message + ": " + instance.toString)
    instance
  end log

  def PureconfigBasics_Program: IO[Unit] = for {
    fib <- IO(println("/Users/drmark/IdeaProjects/PLANE/src/main/scala/CatsIO/ExternalFrameworks/PureconfigBasics.scala")).start
    cfgA <- loadFEither[IO, MySql]
    cfgB <- loadFThrow[IO, MySql]
    cfgC <- loadF[IO, MySql]
    l1 = log(s"time: 10/4/25: ${fib.toString}".blue, "")
    l2 = log(s"configuration: $cfgA".blue, " -- loaded with loadFEither")
    l3 = log(s"configuration: $cfgB".blue, " -- loaded with loadFThrow")
    l4 = log(s"configuration: $cfgC".blue, " -- loaded with loadF")
    _ <- fib.join
  } yield ()

  def loadFEither[F[_] : MonadThrow, A: ConfigReader : ClassTag]: ConfigSource ?=> F[A] =
    MonadThrow[F].fromEither(
      summon[ConfigSource].load[A].leftMap(errs => ConfigReaderException(errs): Throwable)
    )

  def loadF[F[_] : MonadThrow, A: ConfigReader : ClassTag]: ConfigSource ?=> F[A] =
    MonadThrow[F].catchNonFatal(summon[ConfigSource].loadOrThrow[A])

  def loadFThrow[F[_] : MonadThrow, A: ConfigReader : ClassTag]: ConfigSource ?=> F[A] =
    MonadThrow[F].catchNonFatal(summon[ConfigSource].loadOrThrow[A])

  final case class ConnectionParams(
                                     useSSL: Boolean = false,
                                     serverTimezone: String = "UTC",
                                     allowPublicKeyRetrieval: Boolean = true,
                                     connectionPoolSize: Int = 10,
                                     connectionTimeout: Int = 30000,
                                     idleTimeout: Int = 600000
                                   )

  object ConnectionParams:
    given ConfigReader[ConnectionParams] = deriveReader

  final case class MySql(
                          url: String = "jdbc:mysql://localhost:3306/board",
                          user: String = "root",
                          driver: String = "com.mysql.cj.jdbc.Driver",
                          connectionParams: ConnectionParams = ConnectionParams()
                        )

  object MySql:
    given ConfigReader[MySql] = deriveReader

  given ConfigSource = ConfigSource.default.at("MySql")

  //val cfg1: IO[MySql] = loadFEither[IO, MySql]

  override def run(args: List[String]): IO[ExitCode] =

      ConfigSource.default.at("MySql").load[MySql] match
        case Left(value) => println(s"Failed to load config: $value".red.bold)
        case Right(value) => println(s"Loaded config: $value".green.bold)

      PureconfigBasics_Program.as(ExitCode.Success)
