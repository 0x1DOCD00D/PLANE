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

import DesignPatterns.BuilderPatterns.{Object2Build, SomeObj1}

object BuilderPatterns {

  //the simplest implementation of the pattern Builder is with the case class
  case class Object2Build(val p1: Int, val p2: SomeObj1, val p3: Tuple2[Float, List[String]]) {
    //we may add constraints to make sure that the object is initialized properly
    require(p1 > 0)
    require(p2.p1.isEmpty != true)
    require(p3 != null && p3._2.isEmpty != true)

  }

  case class SomeObj1(val p1: String)

  val object1 = Object2Build(p2 = SomeObj1(p1 = "Stuff"), p1 = 1, p3 = (1.2f, List("Howdy")))
}

//in this implementation of the builder pattern we show how the make the Scala compiler
//to type check the correctness of the object construction
object NowWeWantToCheckIfAllValuesAreProvided {

  sealed trait SetValue

  sealed trait SetP1 extends SetValue

  sealed trait SetP2 extends SetValue

  sealed trait SetP3 extends SetValue

  class ObjectBuilder[SetV <: SetValue] private(var p1: Int, var p2: SomeObj1, var p3: Tuple2[Float, List[String]]) {
    def this() = this(p2 = SomeObj1(p1 = "Stuff"), p1 = 1, p3 = (1.2f, List("Howdy")))

    protected def this(ob: ObjectBuilder[_]) = this(ob.p1, ob.p2, ob.p3)

    //sealed abstract class =:=[From, To] extends (From <:< To) with Serializable
    def setP1(pp1: Int)(implicit ev: SetV =:= Nothing): ObjectBuilder[SetP1] = {
      p1 = pp1
      new ObjectBuilder[SetP1](this)
    }

    def setP2(pp2: SomeObj1)(implicit ev: SetV =:= SetP1): ObjectBuilder[SetP2] = {
      p2 = pp2
      new ObjectBuilder[SetP2](this)
    }

    def setP3(pp3: Tuple2[Float, List[String]])(implicit ev: SetV =:= SetP2): ObjectBuilder[SetP3] = {
      p3 = pp3
      new ObjectBuilder[SetP3](this)
    }

    def build(implicit ev: SetV =:= SetP3) = Object2Build(p1, p2, p3)
  }

}

object RunIt extends App {

  import NowWeWantToCheckIfAllValuesAreProvided._

  val obj = new ObjectBuilder[Nothing].setP1(2).setP2(SomeObj1("Stuff")).setP3((1.2f, List("Howdy"))).build
  //val objWrong1 = new ObjectBuilder[SetValue].setP1(2).setP2(SomeObj1("Stuff")).setP3((1.2f,List("Howdy"))).build
  //val objWrong2 = new ObjectBuilder[Nothing].setP2(SomeObj1("Stuff")).setP3((1.2f,List("Howdy"))).build
}