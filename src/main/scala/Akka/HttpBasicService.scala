/*
 * Copyright (c) 2023 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Akka

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors

import scala.concurrent.Future
import akka.actor.typed.scaladsl.AskPattern.*
import akka.util.Timeout

import scala.util.Failure
import scala.util.Success
import scala.io.StdIn

object HttpBasicService:
  private def startHttpServer(routes: Route)(using system: ActorSystem[?]): Unit = {
    import system.executionContext

    val futureBinding = Http().newServerAt("localhost", 8080).bind(routes)
    futureBinding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info("Server online at http://{}:{}/", address.getHostString, address.getPort)
      case Failure(ex) =>
        system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
        system.terminate()
    }
  }

//  http://localhost:8080/hello
  @main def runHttpBasicService(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Akka/HttpBasicService.scala created at time 2:14 PM")

    val route: Route =
      path("hello") {
        get {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
        }
      }
    val rootBehavior: Behavior[Nothing] = Behaviors.setup[Nothing] { context =>
      val anActor = context.spawn(
        Behaviors.receiveMessage {
          m =>
            println(m)
            Behaviors.same
        }, "basichttpservice")
      context.watch(anActor)
      Behaviors.empty
    }

    given ActorSystem[Nothing] = ActorSystem[Nothing](rootBehavior, "HelloAkkaHttpServer")
    startHttpServer(route)

