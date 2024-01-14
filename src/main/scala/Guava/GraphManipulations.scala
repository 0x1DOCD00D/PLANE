/*
 * Copyright (c) 2023 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Guava

import com.google.common.graph.{Graphs, MutableValueGraph, ValueGraphBuilder}

object GraphManipulations:
  case class GraphNode(id: Int):
    val properties: List[Int] = List.fill(scala.util.Random.nextInt(10))(scala.util.Random.nextInt(100))
    val childrenObjects: List[GraphNode] = if id <= 5 then
      List.tabulate(scala.util.Random.nextInt(5))(cid => GraphNode(id+1))
    else List.empty

  case class Edge(cost: Double)
  @main def runGraphManipulations(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Guava/GraphManipulations.scala created at time 9:47 AM")
    val graph1: MutableValueGraph[GraphNode, Edge] = ValueGraphBuilder.directed().build()
    val graph2: MutableValueGraph[GraphNode, Edge] = ValueGraphBuilder.directed().build()
    val node1 = GraphNode(1)
    val node2 = GraphNode(2)
    val node3 = GraphNode(3)
    val node4 = GraphNode(4)
    val edge12 = Edge(0.12)
    val edge23 = Edge(0.23)
    val edge31 = Edge(0.31)

    if !graph1.addNode(node1) then
      println(s"Node $node1 already exists")
    if !graph1.addNode(node2) then
      println(s"Node $node2 already exists")
    if !graph1.addNode(node3) then
      println(s"Node $node3 already exists")
    graph1.putEdgeValue(node1, node2, edge12)
    graph1.putEdgeValue(node2, node3, edge23)

    graph2.addNode(node1)
    graph2.addNode(node2)
    graph2.addNode(node3)
    graph2.putEdgeValue(node1, node2, edge12)
    graph2.putEdgeValue(node3, node1, edge31)

    val newGraph2 = Graphs.copyOf(graph2)
    println(graph1)
    println(graph2)
    println(newGraph2)
    graph2.removeNode(node3)

    if !graph2.addNode(node4) then
      println(s"Node $node4 already exists")
    println(graph1)
    println(graph2)
    println(newGraph2)

