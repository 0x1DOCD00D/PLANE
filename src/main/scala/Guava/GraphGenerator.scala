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
import scala.util.Random

object UniformProbGenerator:
  private var offset = 0
  private val probList: LazyList[Double] = generateUniformProbabilities()
  def apply(szOfValues: Int): Iterator[Double] =
    val lst = probList.slice(offset, offset + szOfValues)
    offset += szOfValues
    lst.iterator

  private def generateUniformProbabilities(range: Double = 100.0d): LazyList[Double] = (scala.util.Random.nextInt(range.toInt).toDouble / range) #:: generateUniformProbabilities(range)

class GapModel(val statesTotal: Int, val initStates: Int, val maxBranchingFactor: Int, val maxDepth: Int, val maxProperties: Int, val propValueRange:Int, val actionRange: Int):
  require(statesTotal > 0, "The total number of states must be positive")
  require(initStates > 0, "The number of initial states must be greater than zero")
  require(maxBranchingFactor > 0, "The maximum branching factor must be greater than zero")
  require(maxDepth > 0, "The maximum depth must be greater than zero")
  require(maxProperties > 0, "The maximum number of properties must be greater than zero")
  require(propValueRange > 0, "The range of property values must be greater than zero")
  require(actionRange > 0, "The range of actions must be greater than zero")

  //noinspection UnstableApiUsage
  type GuiStateMachine = MutableValueGraph[GuiObject, Action]
  case class GapGraph(sm: GuiStateMachine, initStates: Array[GuiObject]):
    def degrees: List[(Int, Int)] = sm.nodes().asScala.toList.map(node=>(sm.inDegree(node), sm.outDegree(node)))
    def totalNodes: Int = sm.nodes().asScala.count(_=>true)
    def adjacencyMatrix: Array[Array[Int]] =
      val nodes: Array[GuiObject] = sm.nodes().asScala.toArray
      val matrix: Array[Array[Int]] = Array.ofDim[Int](nodes.length, nodes.length)
      nodes.indices.foreach(i=>
        nodes.indices.foreach(j=>
          if sm.hasEdgeConnecting(nodes(i), nodes(j)) then matrix(i)(j) = 1
          else matrix(i)(j) = 0
        )
      )
      matrix


  private val stateMachine: GuiStateMachine = ValueGraphBuilder.directed().build()

  case class GuiObject(id: Int, children: Int, props: Int, currentDepth:Int = 1):
    val properties: List[Int] = List.fill(props)(scala.util.Random.nextInt(propValueRange))
    val childrenObjects: List[GuiObject] = if currentDepth <= maxDepth then
        List.tabulate(children)(cid=>GuiObject(cid, scala.util.Random.nextInt(maxBranchingFactor), scala.util.Random.nextInt(maxProperties), currentDepth+1))
    else List.empty

    def childrenCount: Int = children + childrenObjects.map(_.childrenCount).sum

  case class Action(actionType: Int, fromId: Int, toId: Int, resultingValue: Option[Int])

  private def createNodes(): Unit =
    (initStates to statesTotal).foreach(id=>
      stateMachine.addNode(GuiObject(id, scala.util.Random.nextInt(maxBranchingFactor), scala.util.Random.nextInt(maxProperties)))
      ()
    )

  private def createAction(from: GuiObject, to: GuiObject): Action =
    val fCount = from.childrenCount
    val tCount = to.childrenCount

    Action(scala.util.Random.nextInt(actionRange),
      scala.util.Random.nextInt(if fCount > 0 then fCount else 1),
      scala.util.Random.nextInt(if tCount > 0 then tCount else 1),
      if scala.util.Random.nextInt(10) % 2 == 0 then None else Some(scala.util.Random.nextInt(propValueRange))
    )
  private def generateProbs4AllNodes(): (Array[GuiObject], Iterator[Double]) =
    val allNodes: Array[GuiObject] = stateMachine.nodes().asScala.toArray
    (allNodes, UniformProbGenerator(allNodes.length*allNodes.length))

//  using this method we create a connected graph where there are no standalone unconnected nodes
  private def linkOrphanedNodesAndInitStates(allNodes: Array[GuiObject]): Unit =
    val orphans: Array[GuiObject] = allNodes.filter(node => stateMachine.incidentEdges(node).isEmpty)
    val connected: Array[GuiObject] = allNodes.filterNot(node => stateMachine.incidentEdges(node).isEmpty)
    orphans.foreach(node=>
      val other = connected(scala.util.Random.nextInt(connected.length))
      stateMachine.putEdgeValue(other, node, createAction(node, other))
    )

  private def addInitStates(allNodes: Array[GuiObject]): Array[GuiObject] =
    (0 until initStates).map(id=>
        val newInitNode:GuiObject = GuiObject(id, scala.util.Random.nextInt(maxBranchingFactor), scala.util.Random.nextInt(maxProperties))
        stateMachine.addNode(newInitNode)
        val connected: Array[GuiObject] = allNodes.filter(node => stateMachine.outDegree(node) > 0)
        connected.foreach(node=>
          stateMachine.putEdgeValue(newInitNode, node, createAction(newInitNode, node))
        )
        newInitNode
    ).toArray

  def generateModel(edgeProbability: Double = 0.3d): GapGraph =
    createNodes()
    val (allNodes, probIterator) = generateProbs4AllNodes()
    allNodes.foreach(node=>
      allNodes.foreach(other=>
        if node != other && probIterator.next() < edgeProbability then
          stateMachine.putEdgeValue(node, other, createAction(node, other))
        else ()
      )
    )
    linkOrphanedNodesAndInitStates(allNodes)
    GapGraph(stateMachine, addInitStates(allNodes))
  end generateModel

  def perturbModel(model: GapGraph, perturbationCoefficient: Double): GapGraph =
    require(perturbationCoefficient > 0 && perturbationCoefficient <= 1, "The perturbation coefficient must be between 0 and 1")
//    model.sm
    null
  end perturbModel


object GraphGenerator:
  @main def runGraphGenerator(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Guava/GraphGenerator.scala created at time 11:42 AM")
    val gapModel = GapModel(100, 1, 8, 7, 10, 1000, 10)
    val graph = gapModel.generateModel()
//    graph.sm.nodes().forEach(println)
//    val arr = graph.initStates.map(n => n.childrenCount)
    println(graph.totalNodes)
    println(graph.degrees)
    println(graph.adjacencyMatrix.map(_.mkString(",")).mkString("\n"))
/*
    val graph:MutableValueGraph[GuiTree, String] = new GapStateMachine {}.generateStateMachine()
    graph.nodes().forEach(println)
    graph.edges().forEach( println)
    println(graph.edgeValue(GuiTree(4), GuiTree(1)).get)
*/
