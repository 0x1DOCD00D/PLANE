/*
 * Copyright (c) 2023 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Guava

import com.google.common.graph.*

import scala.jdk.CollectionConverters.*

object UniformProbGenerator:
  def apply(): LazyList[Double] = generateUniformProbabilities()
  private def generateUniformProbabilities(range: Double = 100.0d): LazyList[Double] = (scala.util.Random.nextInt(range.toInt).toDouble / range) #:: generateUniformProbabilities(range)
class GapModel(val statesTotal: Int, val initStates: Int, val maxBranchingFactor: Int, val maxDepth: Int, val maxProperties: Int, val propValueRange:Int):
  //noinspection UnstableApiUsage
  type GuiStateMachine = MutableValueGraph[GuiObject, Action]

  private val stateMachine: GuiStateMachine = ValueGraphBuilder.directed().build()
  case class GuiObject(id: Int, children: Int, props: Int, currentDepth:Int = 1):
    val properties: List[Int] = List.fill(props)(scala.util.Random.nextInt(propValueRange))
    val childrenObjects: List[GuiObject] = if currentDepth <= maxDepth then
        List.tabulate(children)(cid=>GuiObject(cid, scala.util.Random.nextInt(maxBranchingFactor), scala.util.Random.nextInt(maxProperties), currentDepth+1))
    else List.empty
  case class Action(fromId: Int, toId: Int, resultingValue: Option[Int])
  def generateModel(edgeProbability: Double = 0.2d): GuiStateMachine =
    (1 to statesTotal).foreach(id=>
      stateMachine.addNode(GuiObject(id, scala.util.Random.nextInt(maxBranchingFactor), scala.util.Random.nextInt(maxProperties)))
      ()
    )
    val allNodes: Array[GuiObject] = stateMachine.nodes().asScala.toArray
    val probGenerator:LazyList[Double] = UniformProbGenerator()
    val prob = probGenerator.take(allNodes.length*allNodes.length).iterator
    allNodes.foreach(node=>
      allNodes.foreach(other=>
        if node != other && prob.next() < edgeProbability then
          stateMachine.putEdgeValue(node, other, Action(node.id, other.id, None))
        else ()
      )
    )
    stateMachine


trait GraphGenerator:
  def generateGraph(): MutableValueGraph[String, Int]
object GraphGenerator:
  @main def runGraphGenerator(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Guava/GraphGenerator.scala created at time 11:42 AM")
    val gapModel = GapModel(100, 1, 8, 7, 10, 1000)
    val graph = gapModel.generateModel()
    println(gapModel.generateModel())
/*
    val graph:MutableValueGraph[GuiTree, String] = new GapStateMachine {}.generateStateMachine()
    graph.nodes().forEach(println)
    graph.edges().forEach( println)
    println(graph.edgeValue(GuiTree(4), GuiTree(1)).get)
*/
