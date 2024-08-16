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

object DecoratorStackableTraits extends App {

  //we create a simple paramterized store interface
  trait Storage[T] {
    //internally, we use a list buffer as a mutable data structure to store and remove data
    val store: ListBuffer[T]

    def get: Option[T]

    def add(elem: T): Unit
  }

  //a concrete implementation of the integer storage, nothing special
  class IntegerStorage extends Storage[Int] {
    override val store: ListBuffer[Int] = ListBuffer()

    //simple pattern matching: do nothing if the list is empty, otherwise, get the head and remove it
    override def get: Option[Int] = store match {
      case store if store.isEmpty => None
      case _ => {
        val rv = store.head
        store.remove(0)
        Some(rv)
      }
    }

    override def add(elem: Int): Unit = store += elem
  }
  {
    //testing it
    val store = new IntegerStorage
    store.add(2)
    store.add(3)
    store.get.foreach(println)
    store.get.foreach(println)
    store.get.foreach(println)
    store.add(8)
    store.get.foreach(println)
  }

  //now we want to add functionality on top of it and stack it up
  trait IncrementAddedNumber extends Storage[Int] {
    abstract override def add(elem: Int): Unit = super.add(elem + 1)
  }

  trait TripleAddedNumber extends Storage[Int] {
    abstract override def add(elem: Int): Unit = super.add(elem * 3)
  }
  {
    //testing it
    val store = new IntegerStorage with IncrementAddedNumber with TripleAddedNumber
    store.add(2)
    store.add(3)
    store.get.foreach(println)
    store.get.foreach(println)
    store.get.foreach(println)
    store.add(8)
    store.get.foreach(println)
  }

  {
    //testing it
    val store = new IntegerStorage with TripleAddedNumber with IncrementAddedNumber
    store.add(2)
    store.add(3)
    store.get.foreach(println)
    store.get.foreach(println)
    store.get.foreach(println)
    store.add(8)
    store.get.foreach(println)
  }
}