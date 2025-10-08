
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.ExternalFrameworks

import cats.effect.{ExitCode, IO, IOApp}

import scala.util.{Failure, Success, Try}
import org.http4s.*
import org.http4s.dsl.Http4sDsl
import org.http4s.circe.CirceEntityCodec.*
import io.circe.Codec
import io.circe.generic.semiauto.*
import io.circe.syntax.*
import org.http4s.ember.server.EmberServerBuilder                    // ← provides .asJson extension method

object EndpointCirceTranslation extends IOApp:
  def log[T](message: String, instance: T): T =
    println(message + ": " + instance.toString)
    instance
  end log

  def EndpointCirceTranslation_Program: IO[Unit] = for {
    fib <- IO(println("/Users/drmark/IdeaProjects/PLANE/src/main/scala/CatsIO/ExternalFrameworks/EndpointCirceTranslation.scala")).start
    l1 = log("time: 10/8/25:", fib.toString)
    _ <- fib.join
  } yield ()

  final case class User(name: String, age: Int)

  given Codec.AsObject[User] = deriveCodec

  private val dsl = Http4sDsl[IO];

  import dsl.*

//  http://127.0.0.1:8080/user/sample
  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "user" / "sample" =>
      Ok(User("Ada", 27)) // uses the given encoders from CirceEntityCodec.*
  }

  override def run(args: List[String]): IO[ExitCode] =
    EndpointCirceTranslation_Program *>
      EmberServerBuilder
        .default[IO]
        .withHttpApp(routes.orNotFound)
        .build
        .use(_ => IO.println("Server ready!") *> IO.never)
