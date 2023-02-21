/*
 * Copyright (c) 2023 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Guava

import com.google.common.graph.*

import scala.collection.immutable.TreeSeqMap.OrderBy
import scala.collection.mutable
import scala.jdk.CollectionConverters.*
import scala.util.{Failure, Random, Success, Try}

object UniformProbGenerator:
  private var offset:Int = 0
  private var generator: Random = Random()
  private var probList: LazyList[Double] = generateUniformProbabilities()
  def apply(szOfValues: Int = 1): List[Double] =
    if offset > Int.MaxValue - szOfValues then
      offset = 0
      generator = Random()
      probList = generateUniformProbabilities()
    end if
    val lst: List[Double] = probList.slice(offset, offset + szOfValues).toList
    offset += szOfValues
    lst

  private def generateUniformProbabilities(range: Double = 1000.0d): LazyList[Double] = (generator.nextInt(range.toInt).toDouble / range) #:: generateUniformProbabilities(range)

class GapModel(val statesTotal: Int, val maxBranchingFactor: Int, val maxDepth: Int, val maxProperties: Int, val propValueRange:Int, val actionRange: Int):
  require(statesTotal > 0, "The total number of states must be positive")
  require(maxBranchingFactor > 0, "The maximum branching factor must be greater than zero")
  require(maxDepth > 0, "The maximum depth must be greater than zero")
  require(maxProperties > 0, "The maximum number of properties must be greater than zero")
  require(propValueRange > 0, "The range of property values must be greater than zero")
  require(actionRange > 0, "The range of actions must be greater than zero")

  //noinspection UnstableApiUsage
  type GuiStateMachine = MutableValueGraph[GuiObject, Action]
  case class GapGraph(sm: GuiStateMachine, initState: GuiObject):
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
    def maxOutDegree(): Int = sm.nodes().asScala.map(node=>stateMachine.outDegree(node)).max
    def unreachableNodes(): (Set[GuiObject], Int) =
      def dfs(nodes: List[GuiObject], visited: Set[GuiObject]): (Set[GuiObject], Int) =
        nodes match
          case Nil => (visited, 0)
          case hd::tl => if visited.contains(hd) then (visited, 1) else
            val (nodesHd, loopsHd) = dfs(sm.successors(hd).asScala.toList, visited+hd)
            val (nodesTl, loopsTl) = dfs(tl, visited+hd)
            (nodesHd ++ nodesTl, loopsHd + loopsTl)
      end dfs

      val (reachableNodes:Set[GuiObject], loops:Int) = dfs(sm.successors(initState).asScala.toList, Set())
      val allNodes:Set[GuiObject] = sm.nodes().asScala.toSet
      (allNodes -- reachableNodes -- Set(initState), loops)

    def distances(): Map[GuiObject, Double] =
      val distanceMap: scala.collection.mutable.Map[GuiObject, Double] = collection.mutable.Map() ++ sm.nodes().asScala.map(node=> node -> Double.PositiveInfinity).toMap
      val zeroCost: Double = 0.0d
      val noEdgeCost: Double = Double.NaN
      distanceMap += (initState -> zeroCost)

      def relax(u: GuiObject)(v: GuiObject): Boolean =
        import scala.jdk.OptionConverters.*
        val edgeCost = if sm.hasEdgeConnecting(u,v) then
          sm.edgeValue(u, v).toScala match
            case Some(action) => action.cost
            case None => noEdgeCost
        else noEdgeCost
        if edgeCost.isNaN then false
        else
          if distanceMap(v) > distanceMap(u) + edgeCost then
                distanceMap(v) = distanceMap(u) + edgeCost
                true
          else false
      end relax

      def explore(node: GuiObject): Unit =
        require(node != null, "The GuiObject node must not be null")
        val successors = sm.successors(node).asScala.toList
        val relaxNode: GuiObject => Boolean = relax(node)
        successors match
          case Nil => ()
          case hd :: tl => if relaxNode(hd) then explore(hd)
                           tl.foreach(cn => if relaxNode(cn) then explore(cn) else ())
      end explore

      explore(initState)
      distanceMap.toMap

  private val stateMachine: GuiStateMachine = ValueGraphBuilder.directed().build()

  trait GapGraphComponent
  case class GuiObject(id: Int, children: Int, props: Int, currentDepth:Int = 1) extends GapGraphComponent:
    val properties: List[Int] = List.fill(props)(scala.util.Random.nextInt(propValueRange))
    val childrenObjects: List[GuiObject] = if currentDepth <= maxDepth then
        List.tabulate(children)(cid=>GuiObject(cid, scala.util.Random.nextInt(maxBranchingFactor), scala.util.Random.nextInt(maxProperties), currentDepth+1))
    else List.empty

    def childrenCount: Int = children + childrenObjects.map(_.childrenCount).sum

  case class Action(actionType: Int, fromId: Int, toId: Int, resultingValue: Option[Int], cost: Double) extends GapGraphComponent

  private def createNodes(): Unit =
    (1 to statesTotal).foreach(id=>
      stateMachine.addNode(GuiObject(id, scala.util.Random.nextInt(maxBranchingFactor), scala.util.Random.nextInt(maxProperties)))
      ()
    )

  private def createAction(from: GuiObject, to: GuiObject): Action =
    val fCount = from.childrenCount
    val tCount = to.childrenCount

    Action(scala.util.Random.nextInt(actionRange),
      scala.util.Random.nextInt(if fCount > 0 then fCount else 1),
      scala.util.Random.nextInt(if tCount > 0 then tCount else 1),
      if scala.util.Random.nextInt(10) % 2 == 0 then None else Some(scala.util.Random.nextInt(propValueRange)),
      UniformProbGenerator().head
    )

//  using this method we create a connected graph where there are no standalone unconnected nodes
  private def linkOrphanedNodesAndInitStates(allNodes: Array[GuiObject]): Unit =
    val orphans: Array[GuiObject] = allNodes.filter(node => stateMachine.incidentEdges(node).isEmpty)
    val connected: Array[GuiObject] = allNodes.filterNot(node => stateMachine.incidentEdges(node).isEmpty)
    orphans.foreach(node=>
      val other = connected(scala.util.Random.nextInt(connected.length))
      stateMachine.putEdgeValue(other, node, createAction(node, other))
    )

  private def addInitState(allNodes: Array[GuiObject], connectedness: Int): GuiObject =
    val maxOutdegree = stateMachine.nodes().asScala.map(node=>stateMachine.outDegree(node)).max
    val newInitNode:GuiObject = GuiObject(0, scala.util.Random.nextInt(maxBranchingFactor), scala.util.Random.nextInt(maxProperties))
    stateMachine.addNode(newInitNode)
    val connected: Array[GuiObject] = allNodes.filter(node => stateMachine.outDegree(node) > (if maxOutdegree >= connectedness then connectedness else maxOutdegree - 1))
    connected.foreach(node=>
      stateMachine.putEdgeValue(newInitNode, node, createAction(newInitNode, node)))
    newInitNode

  def generateModel(edgeProbability: Double = 0.3d): GapGraph =
    createNodes()
    val allNodes: Array[GuiObject] = stateMachine.nodes().asScala.toArray
    allNodes.foreach(node=>
      allNodes.foreach(other=>
        if node != other && UniformProbGenerator().head < edgeProbability then
          stateMachine.putEdgeValue(node, other, createAction(node, other))
        else ()
      )
    )
    linkOrphanedNodesAndInitStates(allNodes)
    GapGraph(stateMachine, addInitState(allNodes,28))
  end generateModel

  trait Modification
  case class NodeRemoved(node: GuiObject) extends Modification
  case class NodeAdded(node: GuiObject) extends Modification
  case class EdgeRemoved(edge: Action) extends Modification
  case class EdgeAdded(edge: Action) extends Modification

  case class OriginalGapComponent(node: Option[GapGraphComponent])

  type ModificationRecord = Map[OriginalGapComponent, Modification]
//  six types of perturbations: node modified, node removed, edge removed, edge is modified, new node is added with edges, new edge is added to some existing node
  def perturbModel(model: GapGraph, perturbationCoefficient: Double, distancePercentile: Double): (GapGraph, ModificationRecord) =
    require(perturbationCoefficient > 0 && perturbationCoefficient <= 1, "The perturbation coefficient must be between 0 and 1")
    require(distancePercentile > 0 && distancePercentile <= 1, "The distance percentile must be between 0 and 1")
    val distances: Map[GuiObject, Double] = model.distances().toSeq.sortBy(_._2).toMap
    val (minDistance, maxDistance) = (distances.minBy(_._2)._2,distances.maxBy(_._2)._2)
    val modelClone = model.copy()

    def verifyDistance(node: GuiObject): Boolean =
      val range:Double = maxDistance - minDistance
      require(range > 1E-20, "The range of distances must be greater than 0")
      val percentile = (distances(node) - minDistance) / range
      percentile >= distancePercentile

    def chooseNodeRandomlyAtDistance(): GuiObject =
      val nodesAtDistance: Array[GuiObject] = distances.filter(_._2 >= distancePercentile).keys.toArray
      nodesAtDistance(scala.util.Random.nextInt(nodesAtDistance.length))

    def removeNode(node: GuiObject, modification: ModificationRecord):ModificationRecord = if modelClone.sm.removeNode(node) then
      modification + (OriginalGapComponent(Some(node)) -> NodeRemoved(node)) else modification

    def addNode(modification: ModificationRecord):ModificationRecord =
      val newNode: GuiObject = GuiObject(modelClone.sm.nodes().asScala.map(_.id).max + 1, scala.util.Random.nextInt(maxBranchingFactor), scala.util.Random.nextInt(maxProperties))
      val allNodes: Array[GuiObject] = modelClone.sm.nodes().asScala.toArray
      val modz = allNodes.foldLeft(Map[OriginalGapComponent,Modification]())((acc, node) =>
          if UniformProbGenerator().head < perturbationCoefficient && verifyDistance(node) then
            val edge = createAction(node, newNode)
            stateMachine.putEdgeValue(node, newNode, edge)
            acc + (OriginalGapComponent(None) -> EdgeAdded(edge))
          else acc
        )
      modification ++ modz + (OriginalGapComponent(None) -> NodeAdded(newNode))

    def modifyNode(modification: ModificationRecord): ModificationRecord =
      import scala.jdk.OptionConverters.*
      val allNodes: Array[GuiObject] = modelClone.sm.nodes().asScala.toArray
      val victim:GuiObject = allNodes(scala.util.Random.nextInt(allNodes.length))
      val newNode: GuiObject = GuiObject(victim.id, scala.util.Random.nextInt(maxBranchingFactor), scala.util.Random.nextInt(maxProperties))
      val pred = modelClone.sm.predecessors(victim).asScala
      val succ = modelClone.sm.successors(victim).asScala
      modelClone.sm.addNode(newNode)
      pred.foreach(node=>
        modelClone.sm.edgeValue(node, victim).toScala match
        case Some(edge) =>
          modelClone.sm.removeEdge(node, victim)
          modelClone.sm.putEdgeValue(node, newNode, edge)
          ()
        case None => ()
      )
      succ.foreach(node =>
        modelClone.sm.edgeValue(victim, node).toScala match
          case Some(edge) =>
            modelClone.sm.removeEdge( victim, node)
            modelClone.sm.putEdgeValue( newNode, node, edge)
            ()
          case None => ()
      )
      modification + (OriginalGapComponent(Some(victim))->NodeRemoved(victim)) +  (OriginalGapComponent(Some(victim)) -> NodeAdded(newNode))


    def removeEdge(node: GuiObject, other:GuiObject, modification: ModificationRecord):ModificationRecord =
      val edge = modelClone.sm.removeEdge(node, other)
      if edge == null then modification else
        modification + (OriginalGapComponent(Some(edge)) -> EdgeRemoved(edge))

    def modifyEdge(node: GuiObject, other: GuiObject, modification: ModificationRecord):ModificationRecord =
      val edge = modelClone.sm.removeEdge(node, other)
      if edge == null then modification else
        val newEdge: Action = edge.copy(resultingValue = Some(scala.util.Random.nextInt(propValueRange)), cost = UniformProbGenerator().head)
        Try(modelClone.sm.putEdgeValue(node, other, newEdge)) match
          case Success(_) => modification + (OriginalGapComponent(Some(edge)) -> EdgeAdded(newEdge)) + (OriginalGapComponent(Some(edge)) -> EdgeRemoved(edge))
          case Failure(_) => modification + (OriginalGapComponent(Some(edge)) -> EdgeRemoved(edge))

    null
//    (modelClone, removedEntities.toSet)
  end perturbModel


object GraphGenerator:
  @main def runGraphGenerator(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Guava/GraphGenerator.scala created at time 11:42 AM")
    val gapModel = GapModel(100, 8, 7, 10, 1000, 10)
    val graph = gapModel.generateModel()
    println(graph.totalNodes)
    println(graph.degrees)
    println(graph.maxOutDegree())
    val (nodes, loops) = graph.unreachableNodes()
    println(s"Unreachable nodes: ${nodes.toList.length}")
    nodes.foreach(println)
    println(s"Loops: $loops")
    println(graph.adjacencyMatrix.map(_.mkString(",")).mkString("\n"))
    graph.distances().toSeq.sortBy(_._2).foreach(println)