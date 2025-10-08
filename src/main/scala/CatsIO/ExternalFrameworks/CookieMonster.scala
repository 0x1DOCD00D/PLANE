////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.ExternalFrameworks

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits.toSemigroupKOps

import scala.util.{Failure, Success, Try}
import org.http4s.headers.*
import org.http4s.{Credentials, HttpRoutes, ResponseCookie, SameSite, StaticFile}
import org.http4s.dsl.{Http4sDsl, RequestDslBinCompat}
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits.uri

object CookieMonster extends IOApp:
  def log[T](message: String, instance: T): T =
    println(message + ": " + instance.toString)
    instance
  end log

  def CookieMonster_Program: IO[Unit] = for {
    fib <- IO(println("/Users/drmark/IdeaProjects/PLANE/src/main/scala/CatsIO/ExternalFrameworks/CookieMonster.scala")).start
    l1 = log("time: 10/8/25:", fib.toString)
    _ <- fib.join
  } yield ()

  val dsl: Http4sDsl[IO] & RequestDslBinCompat = Http4sDsl[IO]; import dsl.*

  val cookieRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case req@GET -> Root / "whoami" =>
      val user = req.cookies.find(_.name == "user").map(_.content).getOrElse("guest")
      Ok(s"Hello, $user").map(_.addCookie(ResponseCookie("lastSeen", System.currentTimeMillis.toString)))

//      curl -X POST http://127.0.0.1:8080/login/drmark
    case POST -> Root / "login" / user =>
      Ok(s"Welcome $user").map(_.addCookie(ResponseCookie("user", user, httpOnly = true, secure = true)))

    case req@GET -> Root / "inspect" =>
      val ua = req.headers.get[`User-Agent`].map(_.product).getOrElse("unknown-UA")
      val auth = req.headers.get[Authorization].flatMap {
        case Authorization(Credentials.Token(_, token)) => Some(s"token:${token.take(6)}…")
        case _ => None
      }.getOrElse("no-auth")
      Ok(s"ua=$ua, auth=$auth")

    case req@GET -> Root / "assets" / path =>
      StaticFile.fromResource[IO](s"/public/$path", Some(req)).getOrElseF(NotFound())
  }

  val whoami: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case req@GET -> Root / "whoami" =>
      val user = req.cookies.find(_.name == "user").map(_.content).getOrElse("guest")
      Ok(s"Hello, $user.")
  }

  val login: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case POST -> Root / "login" / user =>
      val cookie = ResponseCookie(
        name = "user",
        content = user,
        httpOnly = true,
        secure = false, // important for http on localhost
        path = Some("/"),
        sameSite = Some(SameSite.Lax)
      )
      SeeOther(Location(uri"/whoami")).map(_.addCookie(cookie))
  }

  override def run(args: List[String]): IO[ExitCode] =
    val appRoutes4Cookies = (whoami <+> login).orNotFound
    CookieMonster_Program.as(ExitCode.Success) *>
      EmberServerBuilder
        .default[IO]
        .withHttpApp(appRoutes4Cookies)
        .build
        .use(_ => IO.println("Server ready!") *> IO.never)
