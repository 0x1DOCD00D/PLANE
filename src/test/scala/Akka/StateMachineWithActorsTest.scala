/*
 * Copyright (c) 2022 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Akka

import akka.actor.ActorSystem
import akka.testkit.{TestActorRef, TestProbe}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.{AnyWordSpec, AnyWordSpecLike}

import scala.concurrent.duration.*

class StateMachineWithActorsTest extends AnyWordSpecLike with BeforeAndAfterAll {

  given system:ActorSystem = ActorSystem("LetsDoSomeSynchronousTesting")

  override def afterAll(): Unit = summon[ActorSystem].terminate()

  "Some basic sync tests" must {
    "verify a response from a client" in {
      import StateMachineWithActors.*
      val client = TestActorRef[Client](Client())
      val probe = TestProbe()

      probe.send(client, Response("digger"))
      probe.expectMsg(Duration.Zero, StopTheApp("digger"))
    }
  }


  }
