/*
 * Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package DSLWorkshop

import dotty.tools.dotc.util.MutableMap

import scala.language.postfixOps

object VarAssignments:

  import scala.Dynamic
  import scala.language.dynamics

  class I2

  class Res(i: I2) extends Dynamic {
    infix def selectDynamic(obj: String): String = {
      println(s"selectDynamic: $obj")
      allVars += (obj -> "")
      obj
    }
  }

  class V1 {
    def i1(i2: I2): Res = new Res(i2)
  }

  val v1 = new V1
  val i2 = new I2
  val defineVar: Res = v1 i1 i2
  val LET: Res = defineVar
  val EnvTable: Res = defineVar
  val allVars: scala.collection.mutable.Map[String, String] = scala.collection.mutable.Map.empty[String, String]

  def declare(variableBlock: => String): Unit = variableBlock

  extension (key: String)
    infix def <--(v:String): Unit =
      println(s"Assigning $v to $key")
      allVars(key) = v

    infix def :=(v: Double): Unit =
      println(s"Assigning $v to $key")
      allVars(key) = v.toString


  def main(args: Array[String]): Unit = {
    import scala.language.postfixOps
    import VarAssignments.*

    (defineVar newVariable3) <-- "3"

    {
      defineVar newVariable1;
      defineVar newVariable2
    } //<-- "1"

    declare {
      defineVar newVariable3;
      defineVar newVariable4;
      defineVar newVariable5
    }

    (LET newVariable1) := 3.14
    (EnvTable newVariable1) := 3.14

  }
