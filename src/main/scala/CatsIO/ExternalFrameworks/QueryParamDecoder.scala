////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.ExternalFrameworks

import cats.effect.{ExitCode, IO, IOApp}

import scala.util.{Failure, Success, Try}
import org.http4s.dsl.impl.*
import org.http4s.{HttpApp, HttpRoutes, ParseFailure, QueryParamDecoder}
import cats.syntax.all.*
import org.http4s.dsl.{Http4sDsl, RequestDslBinCompat}

import java.time.Year
import scala.util.Try
import org.http4s.dsl.impl.*
import org.http4s.ember.server.EmberServerBuilder

object QueryParamDecoder extends IOApp:
  def log[T](message: String, instance: T): T =
    println(message + ": " + instance.toString)
    instance
  end log

  def QueryParamDecoder_Program: IO[Unit] = for {
    fib <- IO(println("/Users/drmark/IdeaProjects/PLANE/src/main/scala/CatsIO/ExternalFrameworks/QueryParamDecoder.scala")).start
    l1 = log("time: 10/8/25:", fib.toString)
    _ <- fib.join
  } yield ()

  val dsl: Http4sDsl[IO] & RequestDslBinCompat = Http4sDsl[IO]

  import dsl.*

  import org.http4s.QueryParamDecoder.given

  // Use fully-qualified http4s QueryParamDecoder to avoid your local clash
  given yearDecoder: org.http4s.QueryParamDecoder[Year] =
  org.http4s.QueryParamDecoder[Int].emap { y =>
    Try(Year.of(y)).toEither.leftMap(_ =>
      org.http4s.ParseFailure("invalid year", s"$y")
    )
  }

  object Q extends QueryParamDecoderMatcher[String]("q")

  object Page extends OptionalQueryParamDecoderMatcher[Int]("page")

  object YearV extends ValidatingQueryParamDecoderMatcher[Year]("year")

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "search" :? Q(q) +& Page(p) +& YearV(yv) =>
      val page = p.getOrElse(1)
      yv.fold(
        fails => BadRequest(s"Bad 'year': ${fails.toList.map(_.sanitized).mkString(", ")}"),
        y => Ok(s"q=$q, page=$page, year=${y.getValue}")
      )
  }

  val idRoutes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case GET -> Root / "users" / IntVar(id) =>
      Ok(s"User by Int id: $id")

    case GET -> Root / "orders" / UUIDVar(oid) =>
      Ok(s"Order: $oid")
  }


  /*
  Valid: http://127.0.0.1:8080/search?q=distributed+systems&page=2&year=2024
  Missing optional page: http://127.0.0.1:8080/search?q=scala&year=2023
  Invalid year (triggers 400): http://127.0.0.1:8080/search?q=scala&year=abcd

  Int id: http://127.0.0.1:8080/users/58
  UUID id: http://127.0.0.1:8080/orders/550e8400-e29b-41d4-a716-446655440000
  * */
  override def run(args: List[String]): IO[ExitCode] =
    val appRoutes: HttpApp[IO] = (routes <+> idRoutes).orNotFound
    QueryParamDecoder_Program >>
      EmberServerBuilder
        .default[IO]
        .withHttpApp(appRoutes)
        .build
        .use(_ => IO.println("Server ready!") *> IO.never)
