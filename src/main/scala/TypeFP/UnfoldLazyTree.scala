////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package TypeFP

object UnfoldLazyTree:

  import scala.collection.immutable.LazyList

  sealed trait Tree[+A]

  case class Node[A](value: A, children: LazyList[Tree[A]]) extends Tree[A]

  case object Empty extends Tree[Nothing]

  def lazyTreeUnfold[A](seed: A)(next: A => (A, LazyList[A])): LazyList[Tree[A]] =
    LazyList.unfold(seed) { currentSeed =>
      next(currentSeed) match
        case (value, childSeeds) =>
          val children = childSeeds.flatMap(childSeed => lazyTreeUnfold(childSeed)(next))
          Some((Node(value, children), currentSeed))
    }

  def nextFunction(value: Int): (Int, LazyList[Int]) = (value, LazyList(value * 2, value * 2 + 1))

  def printTree[A](tree: LazyList[Tree[A]], depth: Int, currentDepth: Int = 0): Unit = {
    if (currentDepth <= depth) {
      tree.foreach {
        case Node(value, children) =>
          // Print the current node with indentation based on depth
          println("  " * currentDepth + s"$value")
          // Recursively print children
          printTree(children, depth, currentDepth + 1)
        case Empty =>
          println("  " * currentDepth + "Empty")
      }
    }
  }

  @main def runUnfoldLazyTree(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/TypeFP/UnfoldLazyTree.scala created at time 11:40AM")
    val lazyTree: LazyList[Tree[Int]] = lazyTreeUnfold(1)(nextFunction)
    lazyTree.take(30).toList.foreach(println)
    printTree(lazyTree, 3)
