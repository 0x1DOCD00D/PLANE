/*
 * Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package DSLWorkshop

/*
* Define a DSL for creating agent-based simulations for STAGE.
* The DSL should allow the user to define agents, their behaviors, and the environment in which they interact.
  Agents:
    - Groups: # an abstraction that represents a logical collection of agents with a leader.
    - Behaviors: #each agent can be assigned one or more behaviors in response to messages.
    - Channels: #a channel adds some behavior to message transmission and it connects agents via some other agents, if any thus creating a path.
    - Resources: # resources exist within agents whose behaviors access and manipulate them.
  Messages:
  Models:

  An example simulation can be described the following way.
  Agents {
    Agent1 {
    }
    Agent2 {
    }

  }
* */

object StageWorkshop:
  def main(args: Array[String]): Unit = {
    println("Stage Workshop is running...")
  }