/*
 * Copyright (c) 2022 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Akka
import Akka.BasicActorComm.{PrimitiveActor, Professor, Student}
import akka.actor.{ActorSystem, Props}
import akka.testkit.{EventFilter, ImplicitSender, TestKit, TestProbe}
import com.typesafe.config.ConfigFactory
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.{AnyWordSpec, AnyWordSpecLike}

import scala.concurrent.duration.*

class BasicActorCommTest extends TestKit(ActorSystem("TestUniversity", ConfigFactory.load().getConfig("logMsgInterceptorParams")))
  with ImplicitSender
  with AnyWordSpecLike
  with Matchers
  with BeforeAndAfterAll:

  override def afterAll(): Unit = TestKit.shutdownActorSystem(system)
  case object WrongType

  "First try of basic actor tests" must {
    "verify a response from a Professor with an expected message" in {
      val professor = system.actorOf(Professor("DrMark"))
      professor ! "What is the meaning of the Universe?"
      expectMsg("There are never enough time and space for anything!")
    }

    "verify a response from a Professor by expecting an error message" in {
      within(5.second) {
        val professor = system.actorOf(Professor("DrMark"))
        professor ! WrongType
        val reply = expectMsgType[String]
        assert(reply == "Professor received unknown message WrongType")
      }
    }

    "verify a no response from a primitive actor" in {
      val pa = system.actorOf(Props[PrimitiveActor]())
      pa ! WrongType
      expectNoMessage(5.seconds)
    }

    "use a test probe with students and professor" in {
      val student1 = system.actorOf(Student("Schlemiel"))
      val student2 = system.actorOf(Student("Shlepper"))
      val professor = TestProbe("drmark")
      student1 ! InformStudentAboutProfessor(professor.ref, student2)
      professor.expectMsg("What is the meaning of the Universe?")
    }

    "intercept log entries" in {
      EventFilter.error(message="Student received unknown message", occurrences = 1) intercept {
        val student = system.actorOf(Student("Schlemiel"))
        student ! 1
      }
    }

  }
