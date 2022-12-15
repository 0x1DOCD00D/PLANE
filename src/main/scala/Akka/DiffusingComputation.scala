/*
 * Copyright (c) 2022 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Akka

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
object DiffusingComputation:
  case class CreateDiffCompTree(level: Int, maxDepth: Int)
  case class Workload(w: Double)
  case object ProcessedResult

  class TreeNode(parent: Option[ActorRef]) extends Actor with ActorLogging:
    override def receive: Receive =
      case CreateDiffCompTree(level, maxDepth) =>
        if level <= maxDepth-1 then //this is a parent branch node
          val left = context.actorOf(TreeNode(Option(self)))
          val right = context.actorOf(TreeNode(Option(self)))
          context.become(childrenDefined(left, right))
          if level+1 <= maxDepth-1 then //we are not at the leaf level yet, so keep creating the tree
            log.info("Created level {} branch node {} with left child {} and right child {}", level+1, self.path, left.path, right.path)
            left ! CreateDiffCompTree(level+1, maxDepth)
            right ! CreateDiffCompTree(level+1, maxDepth)
          else
            log.info("Created a leaf node {} at level {}", left.path, level+1)
            log.info("Created a leaf node {} at level {}", right.path, level+1)
      case Workload(x) =>
        log.info (s"leaf node ${context.self.path} processes workload {} from ${sender().path}", x)
        Thread.sleep(500)
        parent match
          case Some(ar) => ar ! ProcessedResult
          case None => log.debug("Oxymoron: the parentless node received a result from its child {}", sender().path)
      case _ => log.error("Actor received unknown message")

    def childrenDefined(left: ActorRef, right: ActorRef): Receive =
      case ProcessedResult => parent match
        case Some(ar) =>
          log.info("The branch node {} received a result from its child {}", self.path, sender().path)
          ar ! ProcessedResult
        case None => log.info("The root node {} received a result from its child {}", self.path, sender().path)

      case Workload(x) =>
        log.info (s"${context.self.path} received workload {} from ${sender().path}", x)
        left ! Workload(x/2)
        right ! Workload(x/2)
      case _ => log.error(s"Actor received unknown message from ${sender().path}")

  object TreeNode:
    def apply(parent: Option[ActorRef]):Props = Props(new TreeNode(parent))
  @main def runDiffusingComputation(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Akka/DiffusingComputation.scala created at time 9:36 AM")
    val actorSystem: ActorSystem = ActorSystem("DiffusingComputation")
    val rootNode = actorSystem.actorOf(TreeNode(None), "root")
    rootNode ! CreateDiffCompTree(0, 3)
    Thread.sleep(10000)
    rootNode ! Workload(1000.0d)
    Thread.sleep(30000)
    actorSystem.terminate()

