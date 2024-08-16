/*
 * Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package FPIntro

import scala.Dynamic
import scala.language.dynamics
import scala.language.postfixOps

case class GenericMessageTemplate(
   name: String,
   values: List[Double] = List(),
   fields: Option[List[GenericMessageTemplate]] = None
)

class KeywordTemplate4String extends Dynamic {
  infix def selectDynamic(name: String): String = name
}

object PatternMatchWithDynamic {
  val dispatch = new KeywordTemplate4String
  val msgX: String = dispatch X
  val msgY: String = dispatch Y
  val msgZ: String = dispatch Z

  def doMatch(map: (String, () => Unit)): PartialFunction[Any, Unit] = {
    case GenericMessageTemplate(name, v, f) if name == map._1 => map._2()
  }

  @main def runPatternMatchWithDynamic(args: String*): Unit =
    println(
       "File /Users/drmark/IdeaProjects/PLANE/src/main/scala/FPIntro/PatternMatchWithDynamic.scala created at time 12:52PM"
    )
    val msg1 = GenericMessageTemplate("X", List(1.0, 2.0, 3.0))
    val matchIt = doMatch {
      (dispatch X) -> (() => println("Received message X."))
    } orElse doMatch {
      (dispatch Y) -> (() => println("Received message Y."))
    } orElse doMatch {
      (dispatch Z) -> (() => println("Received message Z."))
    }
    matchIt(GenericMessageTemplate("Y", List(1.0, 2.0, 3.0)))
}
