/*
 * Copyright (c) 2023 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Akka

import akka.Done
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, CoordinatedShutdown, Props}
import akka.util.Timeout

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.{TimeUnit, *}
import scala.concurrent.{Await, ExecutionContext, Future}

object ActorSystemExperiments:
  class MyActor extends Actor with ActorLogging:
    override def receive: Receive =
      case msg: String if msg == "DONE" => log.info(s"Shutting down the system after receiving: $msg")
                                            context.system.terminate()
      case msg: String => log.info(s"Received message: $msg")
      case _ => log.info("Received unknown message")

  object MyActor:
    def apply(): Props = Props(new MyActor)
  @main def runActorSystemExperiments(args: String*): Unit =
    val system = ActorSystem("ActorSystemExperiments")
    system.log.info("Actor system created")
    given ExecutionContext = system.dispatcher
    val myActor: ActorRef = system.actorOf(MyActor(), "MyActor")
    //coordinated shutdown
    system.registerOnTermination(() => println("Actor system terminated"))
    CoordinatedShutdown(system).addTask(CoordinatedShutdown.PhaseBeforeServiceUnbind, "someTaskName") { () =>
       given Timeout = Timeout(5.seconds)
       system.log.info("Coordinated shutdown task started")
       Future {
          Done
        }
    }
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Akka/ActorSystemExperiments.scala created at time 1:49 PM")
    Thread.sleep(3000)
    system.log.info(s"Actor system uptime is ${system.uptime}")
    system.log.info(s"Actor system scheduler's maxFrequency is ${system.getScheduler.maxFrequency}")
    myActor ! "Hello"
    myActor ! "Hello again"
    myActor ! "Bye!!"
    myActor ! "DONE"
    Await.ready(system.whenTerminated, Duration(10, TimeUnit.SECONDS))
