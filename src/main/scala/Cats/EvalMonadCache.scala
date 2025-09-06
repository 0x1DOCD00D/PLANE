
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats

object EvalMonadCache:

  import cats.Eval

  def expensiveApiCall(): String =
    println("Calling API...")
    Thread.sleep(500)
    "data-from-api"

  val cachedCall: Eval[String] = Eval.later(expensiveApiCall())

  @main def runEvalMonadCache(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Cats/EvalMonadCache.scala created at time 3:11PM")
    println(cachedCall.value) // First time triggers API call
    println(cachedCall.value) // Second time uses cached result

