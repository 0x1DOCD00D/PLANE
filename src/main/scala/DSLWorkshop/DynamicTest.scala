/*
 * Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package DSLWorkshop

import DSLWorkshop.DynamicTest.InitialState.transitions

import scala.List


object DynamicTest:

  import scala.Dynamic
  import scala.language.dynamics
  import scala.language.postfixOps

  trait StageObject

  class Agent extends StageObject:
    infix def has[T](defAgent: T): T = defAgent

  class Behavior extends StageObject:
    infix def responds[T](code: T): Behavior =
      println(s"Behavior responds to $code")
      this

    infix def contains[T](code: T): Behavior =
      this

  trait Destination

  case object to extends Destination:
    infix def apply[T](newTrigger: T): Destination =
      new Destination {}

  case object assigned extends Destination:
    infix def apply[T](values: T*): Destination =
      values.foreach(println(_) )
      new Destination {}

  case object SELECT extends Destination:
    infix def apply[T](values: T*): From =
      values.foreach(println(_))
      new From

  class From {
    infix def FROM[T](values: T*): Destination =
      values.foreach(println(_))
      new Destination {}
  }

  class State extends StageObject:
    infix def behaves(behavior: Behavior): State = new State

    infix def transitions(where: Destination): Destination = where

  case object InitialState extends State

  class Message extends StageObject:
    infix def has[T](defMessage: T): T = defMessage
    infix def :=[T](someValue: T): Message = this

  class Field extends StageObject:
    infix def is(what: Destination): Destination = what
    infix def of(msg: => Message): Field = this
    infix def :=[T](someValue: T): Field = this

  class ProbDistribution extends StageObject

  class Select extends StageObject
  class Table extends StageObject

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

  class MessageConstruct extends Dynamic {
    infix def selectDynamic(name: String): Message = {
      println(s"message construct: $name")
      new Message()
    }
  }

  class FieldConstruct extends Dynamic {
    infix def selectDynamic(name: String): Field = {
      println(s"field construct: $name")
      new Field
    }
  }

  class ProbDistributionConstruct extends Dynamic {
    infix def selectDynamic(name: String): ProbDistribution = {
      println(s"probabilistic distribution construct: $name")
      new ProbDistribution
    }
  }
//(classType: Class[T]): Option[T] = Some(classType.getDeclaredConstructor().newInstance())
  class GenericConstruct[T <: StageObject](classType: Class[T]) extends Dynamic {
    infix def selectDynamic(name: String): T = {
      println(s"${classType.getName} construct: $name")
      classType.getDeclaredConstructor().newInstance()
    }
  }

  def main(args: Array[String]): Unit = {
    val agent = new GenericConstruct(classOf[Agent])
    val state = new GenericConstruct(classOf[State])
    val behavior = new GenericConstruct(classOf[Behavior])
    val message = new GenericConstruct(classOf[Message])
    val field = new GenericConstruct(classOf[Field])
    val distribution = new GenericConstruct(classOf[ProbDistribution])
    val attribute = new GenericConstruct(classOf[Select])
    val table = new GenericConstruct(classOf[Table])

    SELECT ((attribute attr1), (attribute attr2), (attribute attr3)) FROM((table SomeTable))

    (agent process1) has {
      InitialState behaves {
        (behavior behavior1) contains {
          println("agent process1's behavior in the init state")
        }
      } transitions to(state newState);
      (state newState) behaves {
        (behavior behavior1) contains {
          println("agent process1's behavior in the new state")
        }
      } transitions to(state newState1);
      (state newState1) transitions to (state newState2)
    }

    (agent process2) has {
      println("process2")
    }

    (message message1) has {
      (field f1) is assigned ("a", 2, 3);
      (field f2);
      (field f1) is assigned (distribution uniform1);
    }

    (behavior b1) responds to {
      (message message1);
      (message message2)
    } contains {
      println("implementation of behavior b1")
      (field f1) of (message message1) := "value1"
      val x = (field f1) of (message message2)
    }

  }