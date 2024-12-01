////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package TypeFP

import scala.annotation.tailrec

object UnfoldingIt:
  def unfold_1[A, S](initState: S)(f: S => Option[(A, S)]): List[A] = {
    f(initState) match {
      case Some((value, nextState)) => value :: unfold_1(nextState)(f)
      case None                     => Nil
    }
  }

  def unfold_2[A, S](initState: S)(f: S => Option[(A, S)]): Set[A] = {
    @tailrec
    def loop(state: S, acc: Set[A]): Set[A] = {
      f(state) match {
        case Some((value, nextState)) => loop(nextState, acc + value)
        case None                     => acc
      }
    }

    loop(initState, Set.empty)
  }

  case class Tree[A](value: A, children: List[Tree[A]])

  def unfold_3[A, S](initState: S)(f: S => (A, List[S])): Tree[A] = {
    val (value, childStates) = f(initState)
    Tree(value, childStates.map(unfold_3(_)(f)))
  }

  @main def runUnfoldingIt(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/TypeFP/UnfoldingIt.scala created at time 8:34PM")
    val result = unfold_1(1) { state =>
      if (state > 10) None
      else Some((state, state + 1))
    }
    println(result) // Output: List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

    val powersOfTwo = unfold_2(1) { state =>
      if (state > 128) None
      else Some((state, state * 2))
    }
    println(powersOfTwo) // Output: Set(1, 2, 4, 8, 16, 32, 64, 128)

    val tree = unfold_3(1) { state =>
      (state, if (state > 3) Nil else List(state * 2, state * 2 + 1))
    }
    println(tree)
    // Output: Tree(1,List(Tree(2,List(Tree(4,List()), Tree(5,List()))), Tree(3,List(Tree(6,List()), Tree(7,List())))))
