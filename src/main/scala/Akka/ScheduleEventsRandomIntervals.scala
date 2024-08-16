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
import scala.annotation.tailrec
import scala.concurrent.duration.{TimeUnit, *}
import scala.concurrent.{Await, ExecutionContext, Future}

object ScheduleEventsRandomIntervals:
  import scala.util.Random
  val rg = new Random()
  val system: ActorSystem = ActorSystem("ExperimentsWithScheduling")
  system.log.info(s"Actor system created: ${system.name}")
  given ExecutionContext = system.dispatcher

  class MyActor extends Actor with ActorLogging:
    override def receive: Receive =
      case msg: String if msg == "DONE" => log.info(s"Shutting down the system after receiving: $msg")
        Thread.sleep(5000)
        context.system.terminate()
      case msg: Int => log.info(s"Received message: $msg")
                        scheduleEventsWithAkkaScheduler(self, msg+1, 10, 1000, 10000)
      case _ => log.info("Received unknown message")

  object MyActor:
    def apply(): Props = Props(new MyActor)

  @tailrec def scheduleEvents(actor: ActorRef, counter: Int, max: Int, minInterval: Int, maxInterval:Int)(using ec: ExecutionContext): Unit =
    val randomInterval = rg.between(minInterval, maxInterval)
    if counter < max then
      actor ! counter
      Thread.sleep(randomInterval)
      scheduleEvents(actor, counter + 1, max, minInterval, maxInterval)
    else
      actor ! "DONE"

  def scheduleEventsWithAkkaScheduler(actor: ActorRef, counter: Int, max: Int, minInterval: Int, maxInterval: Int): Unit =
    import scala.concurrent.duration.*
    val randomInterval = rg.between(minInterval, maxInterval)
    if counter < max then
      system.log.info(s"Scheduling event $counter in $randomInterval milliseconds")
      system.scheduler.scheduleOnce(Duration(randomInterval, TimeUnit.MILLISECONDS)) {
        actor ! counter
      }
    else
      actor ! "DONE"

  @main def runScheduleEventsRandomIntervals(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Akka/ScheduleEventsRandomIntervals.scala created at time 9:32 AM")
    import akka.pattern.ask

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
  //    scheduleEvents(myActor, 0, 10, 1000, 10000)
    scheduleEventsWithAkkaScheduler(myActor, 0, 10, 1000, 10000)
    Await.ready(system.whenTerminated, Duration(1000, TimeUnit.SECONDS))
