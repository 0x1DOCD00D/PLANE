////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package ReflectionExperiments

object LazyRuntimeEval:

  import dotty.tools.repl.ReplDriver

  var z = 10

  def evaluateCode(code: String): Unit =
    val driver = new ReplDriver(
      Array("-usejavacp"),
      Console.out,
      None
    )

    var state = driver.initialState

    val expressions = code.split("[;\n]").map(_.trim).filter(_.nonEmpty)

    expressions.foreach { expr =>
      try
        state = driver.run(expr)(using state)
      catch
        case ex: Throwable =>
          println(s"Exception during evaluation of '$expr': ${ex.getMessage}")
    }