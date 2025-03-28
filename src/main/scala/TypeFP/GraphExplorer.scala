////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package TypeFP

/*
 * This code is copied from some discussion at the scala user group
 * */

object GraphExplorer:
  case class Node[A, G <: Graph.Aux[A, G]](value: A, neighbors: Set[Node[A, G]])

  trait Graph[A]:
    type This <: Graph.Aux[A, This]
    type N = Node[A, This]

    def nodes: Vector[N]

    def addNode(node: N): Graph.Aux[A, This] = Graph(nodes :+ node)

    def connect(node1: N, node2: N): Graph.Aux[A, This] = Graph(nodes.map {
      case n @ `node1` => n.copy(neighbors = n.neighbors + node2)
      case n @ `node2` => n.copy(neighbors = n.neighbors + node1)
      case n           => n
    })

  object Graph:
    type Aux[A, G <: Graph[A]] = Graph[A]:
      type This = G

    def apply[A, G <: Aux[A, G]](_nodes: Vector[Node[A, G]]): Aux[A, G] = new Graph[A]:
      override type This = G

      override def nodes: Vector[N] = _nodes

    def empty[A]() = new Graph[A]:
      override type This = this.type
      override val nodes: Vector[N] = Vector[N]()

  @main def runGraphExplorer(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/TypeFP/GraphExplorer.scala created at time 9:41AM")
    val graph = Graph.empty[Int]()
    graph: Graph.Aux[Int, graph.type]
    println(graph.nodes) // Vector()
    val node1: graph.N = Node(1, Set())
    val graph1: Graph.Aux[Int, graph.type] = graph.addNode(node1)
    println(graph1.nodes) // Vector(Node(1,Set()))
    val node2: graph.N = Node(2, Set())
    val graph2: Graph.Aux[Int, graph.type] = graph1.addNode(node2)
    println(graph2.nodes) // Vector(Node(1,Set()), Node(2,Set()))
    val node3: graph.N = Node(3, Set())
    val graph3: Graph.Aux[Int, graph.type] = graph2.addNode(node3)
    println(graph3.nodes) // Vector(Node(1,Set()), Node(2,Set()), Node(3,Set()))
    val graph4: Graph.Aux[Int, graph.type] = graph3.connect(node1, node2)
    println(graph4.nodes) // Vector(Node(1,Set(Node(2,Set()))), Node(2,Set(Node(1,Set()))), Node(3,Set()))

    val graph5 = Graph.empty[Int]()
    val node4: graph5.N = Node(100, Set())

//  graph4.addNode(node4) // doesn't compile

