////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.ExternalFrameworks

import cats.effect.*
import cats.syntax.all.*
import org.http4s.*
import org.http4s.dsl.io.*
import org.http4s.implicits.*
import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.ember.server.EmberServerBuilder
import CatsIO.Helpers.{bold, green}

import scala.util.{Failure, Success, Try}

object Http4sServer extends IOApp:
  def log[T](message: String, instance: T): T =
    println(message + ": " + instance.toString)
    instance
  end log

  def Http4sServer_Program: IO[Unit] = for {
    fib <- IO(println("/Users/drmark/IdeaProjects/PLANE/src/main/scala/CatsIO/ExternalFrameworks/Http4sServer.scala")).start
    l1 = log("time: 10/7/25:", fib.toString)
    _ <- fib.join
  } yield ()

  val httpApp = HttpRoutes.of[IO] {
    case GET -> Root / "hello" / name =>
      println(s"Received request with name: $name".green.bold)
      Ok(s"Hello, $name!")
  }.orNotFound

  override def run(args: List[String]): IO[ExitCode] = Http4sServer_Program >>
    EmberServerBuilder
    .default[IO]
    .withHttpApp(httpApp)
    .build
    .use(_ => IO.println("Server ready!") *> IO.never)
