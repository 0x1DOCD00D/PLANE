
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.Evals

object EvalMonadConfigMgmt:
  import cats.Eval
  case class Config(dbUrl: String, cacheSize: Int)

  val defaultCfg: Eval[Config] = Eval.now(Config("localhost", 128))
  val fileCfg: Eval[Config] = Eval.later {
    println("Reading config file"); Config("db.prod", 256)
  }
  val envCfg: Eval[Config] = Eval.always {
    println("Checking ENV"); Config("db.env", 512)
  }

  def mergedConfig: Eval[Config] =
    for
      d <- defaultCfg
      f <- fileCfg
      e <- envCfg
    yield Config(
      dbUrl = if e.dbUrl.nonEmpty then e.dbUrl
      else if f.dbUrl.nonEmpty then f.dbUrl
      else d.dbUrl,
      cacheSize = e.cacheSize.max(f.cacheSize).max(d.cacheSize)
    )

  @main def runEvalMonadConfigMgmt(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Cats/EvalMonadConfigMgmt.scala created at time 9:06AM")
    println("Before requesting config")
    println(mergedConfig.value) // triggers computation
    println(mergedConfig.value) // shows caching vs recomputing
