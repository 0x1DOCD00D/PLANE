/*
 * Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package DSLWorkshop

import DSLWorkshop.DynamicTest.InitialState.transitions


object DynamicTest:

  import scala.Dynamic
  import scala.language.dynamics
  import scala.language.postfixOps

  class Agent:
    infix def has(defAgent: => Any): Any = defAgent

  class Behavior:
    infix def contains(code: => Any): Behavior =
      code
      this

  trait Destination

  case object to extends Destination:
    infix def apply(newState: State): Destination = new Destination {}

  class State:
    infix def behaves(behavior: => Behavior*): State = new State
    infix def transitions(where: Destination): Destination = where

  case object InitialState extends State

  class AgentConstruct extends Dynamic {
    infix def selectDynamic(name: String): Agent = {
      println(s"agent construct: $name")
      new Agent()
    }
  }

  class StateConstruct extends Dynamic {
    infix def selectDynamic(name: String): State = {
      println(s"state construct: $name")
      new State()
    }
  }

  class BehaviorConstruct extends Dynamic {
    infix def selectDynamic(name: String): Behavior = {
      println(s"behavior construct: $name")
      new Behavior()
    }
  }


  def main(args: Array[String]): Unit = {
    val agent = new AgentConstruct
    val state = new StateConstruct
    val behavior = new BehaviorConstruct

    (agent process1) has {
      InitialState behaves {
        (behavior behavior1) contains {
          println("behavior1")
        }
      } transitions to (state newState)
    }
    (agent process2) has {
      println("process2")
    }
  }