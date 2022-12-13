/*
 * Copyright (c) 2022 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Akka

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import akka.event.Logging

object StateMachineWithActors:
  case class Proceed(appRunner: ActorRef)
  case class RunApp(appName: String)
  case class Status(appName: String)
  case class Scheduled(appName: String)
  case class GoAheadAndRunTheApp(appName: String)
  case class StopTheApp(appName: String)

  case class Response(content: String)

  enum States:
    case READY2RUN, RUNNING

  class AppRunner extends Actor with ActorLogging:
    override def receive: Receive =
      case RunApp(appName) =>
        context.become(runningApp(appName, States.READY2RUN))
        log.info("[app runner] received a request to run the app {}", appName)
        sender() ! Scheduled(appName+"wrongapp")
        sender() ! Scheduled(appName)
      case unknown => sender() ! Response(s"unknown message [$unknown] is discarded")
    def runningApp(appName: String, state: States): Receive =
      case Status(an) => if appName == an then log.info("the status of {} is {}", appName, state) else log.error("unknown application $an")
      case GoAheadAndRunTheApp(an) =>
        if appName == an then
          log.info("[app runner] running the app {}", an)
          sender() ! Response(appName)
          context.become(runningApp(appName, States.RUNNING))
        else sender() ! Response("unknown application $an")
      case StopTheApp(an) =>
        if appName == an then
          context.become(receive)
          log.info("[app runner] shutting down the app {}", an)
        else sender() ! Response("unknown application $an")


  object AppRunner:
    def apply(): Props = Props(new AppRunner())

  class Client extends Actor with ActorLogging:
    val appName:String = "digger"
    override def receive: Receive =
      case Proceed(ar) => ar ! RunApp(appName)
      case Scheduled(an) =>
        if an != appName then log.error(s"[client] tried to scheduled a wrong app named {}", an)
        else
          log.info("[client] the {} is scheduled to run", appName)
          Thread.sleep(3000)
          log.info("[client] reminding the app runner to run the app {}", appName)
          sender() ! GoAheadAndRunTheApp(appName)
      case Response(an) => log.info(s"[client:] received response $an from the app runner")
        if an != appName then log.error(s"[client] is informed about an incorrect app reference $an") else sender() ! StopTheApp(appName)

  object Client:
    def apply(): Props = Props(new Client())

  @main def runStateMachineWithActors(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Akka/StateMachineWithActors.scala created at time 11:52 AM")
    val actorSystem: ActorSystem = ActorSystem("stateMachineWithActors")
    val client = actorSystem.actorOf(Client())
    val runner = actorSystem.actorOf(AppRunner())
    client ! Proceed(runner)
    Thread.sleep(10000)
    actorSystem.terminate()


