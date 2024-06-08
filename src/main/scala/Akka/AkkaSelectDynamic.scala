/*
 * Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
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
import scala.Dynamic
import scala.language.dynamics
import scala.language.postfixOps

case class MyRecord(var from: ActorRef) extends Dynamic:
  private val fields = scala.collection.mutable.Map[String, Any]("prop1" -> 1, "prop2" -> "howdy")
  infix def selectDynamic(name: String): Any = fields(name)
  def updateDynamic(name: String)(amount: Any) = fields(name) = amount

object ActorGenericMsgExperiments:
  class MyActor extends Actor with ActorLogging:
    override def receive: Receive =
      case msg: String if msg == "DONE" =>
        log.info(s"Shutting down the system after receiving: $msg")
        Thread.sleep(5000)
        context.system.terminate()
      case rec: MyRecord =>
        log.info(s"Received record: ${rec.prop2}")
        rec.from = self
        rec.prop2 = "Hello from MyActor 1 with a record!"
        rec.from ! rec
      case msg: String => log.info(s"Received message: $msg")
      case _           => log.info("Received unknown message")

  object MyActor:
    def apply(): Props = Props(new MyActor)

  @main def runActorGenericMsgExperiments(args: String*): Unit =
    import akka.pattern.ask
    val system = ActorSystem("ActorSystemExperiments")
    system.log.info("Actor system created")
    //    system.settings.config.getConfig("akka").entrySet().forEach(entry => system.log.info(s"Config entry: ${entry.getKey} -> ${entry.getValue.unwrapped()}"))
    given ExecutionContext = system.dispatcher
    val myActor1: ActorRef = system.actorOf(MyActor(), "MyActor1")
    val myActor2: ActorRef = system.actorOf(MyActor(), "MyActor2")
    // coordinated shutdown
    system.registerOnTermination(() => println("Actor system terminated"))
    CoordinatedShutdown(system)
      .addTask(CoordinatedShutdown.PhaseBeforeServiceUnbind, "someTaskName") { () =>
        given Timeout = Timeout(5.seconds)
        println("Coordinated shutdown task started")
        Future {
          (myActor1 ? 10).mapTo[Unit]
          Done
        }
      }
    println(
       "File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Akka/ActorSystemExperiments.scala created at time 1:49 PM"
    )
    Thread.sleep(3000)
    system.log.info(s"Actor system uptime is ${system.uptime}")
    system.log.info(s"Actor system scheduler's maxFrequency is ${system.getScheduler.maxFrequency}")
    myActor1 ! "Hello"
    myActor1 ! MyRecord(myActor2)
    myActor1 ! "Bye!!"
    myActor1 ! "DONE"
    Await.ready(system.whenTerminated, Duration(10, TimeUnit.SECONDS))
