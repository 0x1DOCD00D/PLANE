/*
 *
 *  * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package DesignPatterns

//we pass an event along the chain of event processors until
//the one is found that can handle this specific event
object ChainOfResponsibility extends App {

  //an event structure can be defined any way you want
  trait Event extends Enumeration {
    type EventType = Value
    val Open, Close, Ignore, NotHandled = Value
    val eventId: Int
    val eventType: EventType
  }

  //the behavior of an event processor is simply to handle an event
  //we will define the actual event handler as a partial function
  trait EventProcessor {
    def handle: PartialFunction[Event, Event]
  }

  //we create a special handler class for each event type that we need to handle
  class OpenEventHandler extends EventProcessor {
    override def handle: PartialFunction[Event, Event] = new PartialFunction[Event, Event] {
      override def isDefinedAt(x: Event): Boolean = if (x.eventType == x.Open) true else false

      override def apply(v1: Event): Event = {
        println("Open")
        v1
      }
    }
  }

  class CloseEventHandler extends EventProcessor {
    override def handle: PartialFunction[Event, Event] = new PartialFunction[Event, Event] {
      override def isDefinedAt(x: Event): Boolean = if (x.eventType == x.Close) true else false

      override def apply(v1: Event): Event = {
        println("Close")
        v1
      }
    }
  }

  class IgnoreEventHandler extends EventProcessor {
    override def handle: PartialFunction[Event, Event] = new PartialFunction[Event, Event] {
      override def isDefinedAt(x: Event): Boolean = if (x.eventType == x.Ignore) true else false

      override def apply(v1: Event): Event = {
        println("Ignore")
        v1
      }
    }
  }

  //this is the event that we should handle
  val event = new Event {
    override val eventId: Int = 1
    override val eventType: EventType = Close
  }

  //we create handler objects
  val openHandler = (new OpenEventHandler).handle
  val closeHandler = (new CloseEventHandler).handle
  val ignoreHandler = (new IgnoreEventHandler).handle

  //and we chain them in a chain of responsibility
  val chain = openHandler orElse closeHandler orElse ignoreHandler
  chain(event)
}
