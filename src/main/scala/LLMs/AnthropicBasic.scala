////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package LLMs

import cats.effect.{ExitCode, IO, IOApp}

import scala.util.{Failure, Success, Try}
import scala.jdk.CollectionConverters.*
import com.anthropic.client.AnthropicClient
import com.anthropic.client.okhttp.AnthropicOkHttpClient
import com.anthropic.models.messages.{Message, MessageCreateParams, Model}
import org.http4s.client

object AnthropicBasic extends IOApp:
  def log[T](message: String, instance: T): T =
    println(message + ": " + instance.toString)
    instance
  end log

  private def extractText(msg: Message): String =
    msg.content().asScala.flatMap(cb => Option(cb.text())).mkString("\n")

  def AnthropicBasic_Program: IO[String] = for {
    fib <- IO(println("/Users/drmark/IdeaProjects/PLANE/src/main/scala/LLMs/AnthropicBasic.scala")).start
    _ <- IO(println("AnthropicBasic_Program starting"))
    _ <- IO(println(s"ANTHROPIC_API_KEY: ${sys.env.get("ANTHROPIC_API_KEY")}"))
    _ <- IO(println("anthropic.apiKey sysprop? " + Option(System.getProperty("anthropic.apiKey")).isDefined))
    _ <- IO(println("key len: " + sys.env.get("ANTHROPIC_API_KEY").map(_.length)))
    _ <- IO(println("sysprop present: " + Option(System.getProperty("anthropic.apiKey")).isDefined))
    _ <- IO(println("base url env: " + sys.env.get("ANTHROPIC_BASE_URL")))
    client <- IO.delay(AnthropicOkHttpClient.fromEnv(): AnthropicClient)
    params <- IO.delay(
      MessageCreateParams.builder()
        .model("claude-3-5-sonnet-20241022") // the ID you verified via curl
        .maxTokens(64L)
        .addUserMessage("How do professors treat one another?")
        .build()
    )

    // blocking network call -> wrap in IO.blocking
    msg <- IO.blocking(client.messages().create(params))

    // extract text blocks into a single string
    text <- IO.delay(extractText(msg))
    l1 = log(s"time: 10/12/25:", fib.toString)
    _ <- fib.join
  } yield text

  override def run(args: List[String]): IO[ExitCode] =
    AnthropicBasic_Program
    .flatMap(reply => IO.println(s"Claude: $reply"))
    .as(ExitCode.Success)
