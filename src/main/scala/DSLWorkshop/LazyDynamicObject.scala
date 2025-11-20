////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package DSLWorkshop

import scala.language.dynamics

class LazyDynamicObject extends Dynamic {
  // Store the blocks for later execution
  private var blocks = Map[String, () => Any]()

  def applyDynamic(methodName: String)(block: => Any): LazyDynamicObject = {
    println(s"Registering method '$methodName'")

    // Store the block as a function
    blocks += (methodName -> (() => block))

    this
  }

  // Execute a stored block
  def execute(methodName: String): Option[Any] = {
    blocks.get(methodName).map { block =>
      println(s"Executing '$methodName':")
      block()
    }
  }
}

@main def runLazyDynamicObject(args: String*): Unit =
  println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/DSLWorkshop/LazyDynamicObject.scala created at time 6:32PM")
  val lazyObj = new LazyDynamicObject

  lazyObj someMethod {
    println("This won't execute immediately")
    "result"
  }

  // Execute later
  lazyObj.execute("someMethod") // Now it executes