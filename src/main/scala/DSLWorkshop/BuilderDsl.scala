/*
 * Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package DSLWorkshop
import scala.language.dynamics
object BuilderDsl:
  @main def mn(args: String*) = {
    val lb = new ListBuilder

    lb.any.method("with", 100, "parameters").you.like

    println(lb.result)
    // List(any, method(with 100 parameters), you, like)
  }


  class ListBuilder extends Dynamic {
    private var res = List[String]()

    infix def selectDynamic(obj: String): ListBuilder = {
      res = obj :: res
      this
    }

    infix def applyDynamic(method: String)(args: Any*) = {
      val argString = if (args.length>0) "(" + args.mkString(" ") + ")" else ""
      res = method + argString :: res
      this
    }
    def result = res.reverse
  }

