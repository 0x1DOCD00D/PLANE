
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.ReaderWriter

import cats.data.ReaderT
import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits.*

object ReaderIO extends IOApp:

  case class Config(baseUrl: String, apiKey: String)
  case class HttpResponse(status: Int, body: String)

  trait HttpClient:
    def get(url: String): IO[HttpResponse]

  class DummyHttpClient extends HttpClient:
    def get(url: String): IO[HttpResponse] =
      IO.println(s"Calling $url") *> IO.pure(HttpResponse(200, s"Response from $url"))

  type App[A] = ReaderT[IO, Config, A]

  def callApi(endpoint: String): App[HttpResponse] =
    ReaderT { config =>
      val url = s"${config.baseUrl}/$endpoint?key=${config.apiKey}"
      IO.println(s"Resolved URL: $url") *> new DummyHttpClient().get(url)
    }

  def program: App[String] = for
    res1 <- callApi("users")
    res2 <- callApi("orders")
  yield s"Users: ${res1.body}, Orders: ${res2.body}"

  override def run(args: List[String]): IO[ExitCode] =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Cats/ReaderWriter/ReaderIO.scala created at time 2:46PM")
    val config = Config("https://api.service.com", "SECRET")
    for
      result <- program.run(config)      // result: String
      _      <- IO.println(result)       // print the actual string
    yield ExitCode.Success

