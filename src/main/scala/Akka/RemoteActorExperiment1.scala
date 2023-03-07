/*
 * Copyright (c) 2023 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Akka

import akka.Done
import akka.actor.{ActorSystem, CoordinatedShutdown, PoisonPill, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.ConfigFactory

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.{Duration, TimeUnit, *}
import scala.concurrent.{Await, ExecutionContext, Future}
import ChitChatMessages.*

object RemoteActorExperiment1 extends App {
  println("remote")
//  [akka://RemoteActorSystem@192.168.1.8:25521] with UID [6678455371721108346] MDC: {akkaAddress=akka://RemoteActorSystem@192.168.1.8:25521, akkaUid=6678455371721108346, sourceThread=main, akkaSource=ArteryTransport(akka://RemoteActorSystem), sourceActorSystem=RemoteActorSystem, akkaTimestamp=20:00:29.328UTC}
  val system = ActorSystem("RemoteActorSystem", ConfigFactory.load("remoteExperiment/remoteExpConfig1.conf").getConfig("remoteApp"))

  given ExecutionContext = system.dispatcher

//  val chatActor = system.actorOf(ChatActor(), "chatActor")
//  chatActor ! StartChitChat
  Thread.sleep(50000)
  system.terminate()
}

object RemoteActorExperiment1_Local extends App {
  println("local")
  val system = ActorSystem("LocalActorSystem", ConfigFactory.load("remoteExperiment/remoteExpConfig1.conf").getConfig("localApp"))
  given ExecutionContext = system.dispatcher

  val chitActor = system.actorOf(ChitActor(), "chitActor")
  val chatActor = system.actorOf(ChatActor(), "chatActor")
  println(chatActor.path)
  system.registerOnTermination(() => println("Local actor system terminated"))
  CoordinatedShutdown(system).addTask(CoordinatedShutdown.PhaseActorSystemTerminate, "someTaskName") { () =>
    given Timeout = Timeout(30.seconds)

    println("Coordinated shutdown task started")
    Future {
      (chitActor ? 10).mapTo[Unit]
      Done
    }
  }
  CoordinatedShutdown(system).addJvmShutdownHook {
    println("RemoteActorExperiment1_Local JVM shutdown hook...")
  }
  Thread.sleep(3000)
  system.log.info(s"Actor system uptime is ${system.uptime}")
  system.log.info(s"Actor system scheduler's maxFrequency is ${system.getScheduler.maxFrequency}")

  chitActor ! StartChitChat
  chatActor ! StartChitChat
  Thread.sleep(100000)
  chitActor ! ChitMessage("Howdy, remote comrade!")
  chitActor ! StopChitChat
  Thread.sleep(1000)
  system.terminate()
  Await.ready(system.whenTerminated, Duration(60, TimeUnit.SECONDS))
}

object RemoteActorExperiment1_Remote extends App {
  println("remote")

}
