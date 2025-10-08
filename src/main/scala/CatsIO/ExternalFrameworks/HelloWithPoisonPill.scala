////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.ExternalFrameworks

import cats.effect.{ExitCode, IO, IOApp}

import scala.util.{Failure, Success, Try}
import cats.effect.*
import cats.syntax.all.*
import com.comcast.ip4s.{ipv4, port}
import org.http4s.*
import org.http4s.dsl.{Http4sDsl, RequestDslBinCompat}
import org.http4s.implicits.*
import org.http4s.ember.server.EmberServerBuilder

import scala.concurrent.duration.*

object HelloWithPoisonPill extends IOApp:
  def log[T](message: String, instance: T): T =
    println(message + ": " + instance.toString)
    instance
  end log

  def HelloWithPoisonPill_Program: IO[Unit] = for {
    fib <- IO(println("/Users/drmark/IdeaProjects/PLANE/src/main/scala/CatsIO/ExternalFrameworks/HelloWithPoisonPill.scala")).start
    l1 = log("time: 10/7/25:", fib.toString)
    _ <- fib.join
  } yield ()

  val dsl: Http4sDsl[IO] & RequestDslBinCompat = Http4sDsl[IO];

  import dsl.*

  // hello route from example (1)
  val hello: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "hello" / name => Ok(s"Hello, $name.")
  }

  object Token extends QueryParamDecoderMatcher[String]("token")

  // POST /admin/shutdown?token=SECRET
  def poisonRoutes(stop: Deferred[IO, Unit], secret: String): HttpRoutes[IO] =
    HttpRoutes.of[IO] {
      case POST -> Root / "admin" / "shutdown" :? Token(t) if t == secret =>
        stop.complete(()).void *> Ok("Shutting down, bye.")
      case POST -> Root / "admin" / "shutdown" =>
        Forbidden("Missing or invalid token.")
    }


  override def run(args: List[String]): IO[ExitCode] =
    HelloWithPoisonPill_Program.as(ExitCode.Success) *>
    Deferred[IO, Unit].flatMap { stop =>
      val secret = "please-dont-hardcode-me"
      val app = (hello <+> poisonRoutes(stop, secret)).orNotFound

      EmberServerBuilder.default[IO]
        .withHost(ipv4"0.0.0.0")
        .withPort(port"8080")
        .withHttpApp(app)
        .withShutdownTimeout(2.seconds)              // optional, gives in-flight requests a moment
        .build
        // server runs until `stop.get` completes, which happens when the route completes the Deferred
        .use(_ => stop.get)
    }.as(ExitCode.Success)
