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

object VisitorPattern extends App {

  //we assume a certain hierarchy of types where they have the same behavior
  //expressed in the method accept that takes one parameter of the type Visitor
  //and it returns true if successful and false, otherwise. The trait Visitor
  //provides the method visit that takes the object of some type, T that represents
  //a type in the hierarchy of types that declare and implement the method accept.
  trait Visitor[T <: {def accept(t: Visitor[T]): Boolean}] {
    def visit(obj: T): Boolean
  }

  //we define the hierarchy of types where all tyoes implement the method accept
  //that takes an object of the type Visitor that is parameterized by the top parent type
  abstract class SomeParent {
    val id = 1

    def accept(t: Visitor[SomeParent]): Boolean = t.visit(this)
  }

  class Child1 extends SomeParent {
    override val id: Int = 2

    override def accept(t: Visitor[SomeParent]): Boolean = t.visit(this)
  }

  class Child2 extends SomeParent {
    override val id: Int = 3

    override def accept(t: Visitor[SomeParent]): Boolean = t.visit(this)
  }

  //Error: type arguments [DesignPatterns.VisitorPattern.Child3] do not conform to trait Visitor's type parameter bounds [T <: AnyRef{def accept(t: DesignPatterns.VisitorPattern.Visitor[T]): Boolean}]
  //    def walk(t: Visitor[Child3]): Boolean = t.visit(this)
  //  class Child3 extends SomeParent {
  //    def walk(t: Visitor[Child3]): Boolean = t.visit(this)
  //  }

  //the visitor object is parameterized by the parent type and it can apply to all of the
  //children objects of the parent type
  val visitor = new Visitor[SomeParent] {
    override def visit(obj: SomeParent): Boolean = {
      println(obj.id)
      obj.id % 2 == 0
    }
  }

  //we create a list of objects
  val listOfChildren = List(new Child1, new Child2, new SomeParent {})
  //and for each of these objects the method accept is invoked and the visitor is passed
  //to perform its job on the object and then we just added some operations to the pipeline
  if (listOfChildren.map(e => e.accept(visitor)).filter(_ == true).isEmpty) println("empty")
  else println("not empty")
}