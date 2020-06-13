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

import scala.collection.mutable.ListBuffer

//we want to model an observer who observes observable events and gets notified when these events occur
object ObserverPattern extends App {

  trait Observer[T] {
    def NotifyWhenEventOccurs(event: T): Boolean
  }

  //the objects of this type will be observed to
  //by all observers that are stored in the internal buffer
  trait Observable[T] {
    private val observerBuffer: ListBuffer[Observer[T]] = ListBuffer()
    private val events: ListBuffer[T] = ListBuffer()

    //stash observers in an internal buffer
    def addObserver(observer: Observer[T]) = observerBuffer += observer

    //put events on the stack
    def addEvent(event: T): Observable[T] = {
      events += event
      this
    }

    def NotifyObservers = {
      observerBuffer.map(observer => {
        events.map(event => if (observer.NotifyWhenEventOccurs(event)) println(s"Observer $observer processed event"))
      })
    }
  }

  case class Event(id: Int)

  class ObserverOfEvents(compare: Int) extends Observer[Event] {
    override def NotifyWhenEventOccurs(event: Event): Boolean = if (event.id == compare) true else false
  }

  class ObservableObject extends Observable[Event]

  val observable = new ObservableObject
  observable.addObserver(new ObserverOfEvents(2))
  observable.addObserver(new ObserverOfEvents(5))
  observable.addObserver(new ObserverOfEvents(10))
  observable.addEvent(new Event(2))
  observable.addEvent(new Event(5))
  observable.addEvent(new Event(10))
  observable.addEvent(new Event(20))
  observable.NotifyObservers
}
