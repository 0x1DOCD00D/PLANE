/*
 * Copyright (c) 2022 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Akka

import akka.actor.{Actor, ActorSystem, Props, ActorRef}

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

  class AppRunner extends Actor:
    override def receive: Receive =
      case RunApp(appName) =>
        context.become(runningApp(appName, States.READY2RUN))
        println(s"[app runner:] received a request to run the app $appName")
        sender() ! Scheduled(appName)
      case unknown => sender() ! Response(s"unknown message [$unknown] is discarded")
    def runningApp(appName: String, state: States): Receive =
      case Status(an) => if appName == an then println(s"the status of $appName is $state") else println("unknown application $an")
      case GoAheadAndRunTheApp(an) =>
        if appName == an then
          println(s"[app runner:] running the app $an")
          sender() ! Response(appName)
          context.become(runningApp(appName, States.RUNNING))
        else sender() ! Response("unknown application $an")
      case StopTheApp(an) =>
        if appName == an then
          context.become(receive)
          println(s"[app runner:] shutting down the app $an")
        else sender() ! Response("unknown application $an")


  object AppRunner:
    def apply(): Props = Props(new AppRunner())

  class Client extends Actor:
    val appName:String = "digger"
    override def receive: Receive =
      case Proceed(ar) => ar ! RunApp(appName)
      case Scheduled(an) =>
        if an != appName then println(s"[error:] $an")
        else
          println(s"[client:] the app $appName is scheduled to run")
          Thread.sleep(3000)
          println(s"[client:] reminding the app runner to run the app $appName")
          sender() ! GoAheadAndRunTheApp(appName)
      case Response(an) => println(s"[client:] received response $an from the app runner")
        if an != appName then println(s"[error:] $an") else sender() ! StopTheApp(appName)

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


