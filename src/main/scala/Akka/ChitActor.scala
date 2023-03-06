/*
 * Copyright (c) 2023 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Akka

import akka.actor.{Actor, ActorIdentity, ActorLogging, Identify, PoisonPill, Props}

class ChitActor extends Actor with ActorLogging {
  //  [akka://RemoteActorSystem@192.168.1.8:25521] with UID [6678455371721108346] MDC: {akkaAddress=akka://RemoteActorSystem@192.168.1.8:25521, akkaUid=6678455371721108346, sourceThread=main, akkaSource=ArteryTransport(akka://RemoteActorSystem), sourceActorSystem=RemoteActorSystem, akkaTimestamp=20:00:29.328UTC}
  override def preStart(): Unit = {
    val selection = context.actorSelection("RemoteActorSystem@192.168.1.8:25521/user/chatActor")
    selection ! Identify("DrMark")
  }

  override def receive: Receive = {
    case m if m.isInstanceOf[StartChitChat.type ]  => log.info(s"Received $m from ${sender()}")
    case m if m.isInstanceOf[StopChitChat.type ]  => log.info(s"Received $m from ${sender()}")
      self ! PoisonPill
      context.system.terminate()
    case ActorIdentity(id, Some(actorRef)) =>
      actorRef ! s"Thank you for identifying yourself, $id!"
    case m => log.info(s"Received $m from ${sender()}")

  }
}

object ChitActor:
  def apply(): Props = Props(new ChitActor())

