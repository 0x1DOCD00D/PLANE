/*
 * Copyright (c) 2022 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Akka

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}

case class InformStudentAboutProfessor(professor: ActorRef, otherStudent: ActorRef)

object BasicActorComm:
  val actorSystem: ActorSystem = ActorSystem("actOnIt1")


  class Professor(name: String) extends Actor with ActorLogging:
    override def receive: Receive =
      case question: String =>
        log.info("Professor {} received the following message: {}", name, question)
//        println(s"[${context.self} received question: ] $question from ${sender().toString()}")
        sender() ! "There are never enough time and space for anything!"
      case msg =>
        log.error("Professor received message {}",msg.toString )
        sender() ! "Professor received unknown message " + msg.toString

  class Student(name: String) extends Actor:
    override def receive: Receive =
      case InformStudentAboutProfessor(prof, stdRef) => prof ! "What is the meaning of the Universe?"
          stdRef forward InformStudentAboutProfessor(prof, self)
      case answer: String => println (s"[${context.self} received an answer from: ${sender().toString()}] - $answer")
      case _ => println("[Student received unknown message]")

  object Student:
    def apply(studentName: String):Props = Props(new Student(studentName))

  object Professor:
    def apply(profName: String): Props = Props(new Professor(profName))

  class PrimitiveActor extends Actor with ActorLogging:
    override def receive: Receive =
      case message: String =>
        val response = "[Primitive actor] received the message "
        log.info(response + message)
        sender() ! response + message
      case _ => log.error("[Primitive actor] doesn't respond to arbitrary messages")

  @main def runBasicActorComm(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Akka/BasicActorComm.scala created at time 9:38 AM")
    println(actorSystem.name + " is created at " + actorSystem.startTime)
    val professor = actorSystem.actorOf(Professor("Mark"))
    val studentA = actorSystem.actorOf(Student("Oy"))
    val studentB = actorSystem.actorOf(Student("Wey"))
    studentA ! 1//should print out [Student received unknown message]
    studentA ! InformStudentAboutProfessor(professor, studentB)
    Thread.sleep(5000)
    actorSystem.terminate()
