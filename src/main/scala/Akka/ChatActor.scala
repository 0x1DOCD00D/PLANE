/*
 * Copyright (c) 2023 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Akka

import akka.actor.{Actor, ActorLogging, PoisonPill, Props}

class ChatActor extends Actor with ActorLogging {
  override def receive: Receive = {
    case m if m.isInstanceOf[StartChitChat.type ]  => log.info(s"Chat received $m from ${sender()}")
    case m if m.isInstanceOf[StopChitChat.type ]  => log.info(s"Chat received $m from ${sender()}")
      self ! PoisonPill
      context.system.terminate()
    case m => log.info(s"Chat received unknown $m from ${sender()}")

  }
}

object ChatActor:
  def apply(): Props = Props(new ChatActor())
