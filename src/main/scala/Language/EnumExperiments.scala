/*
 * Copyright (c) 2023 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Language

import scala.util.{Failure, Success, Try}

object EnumExperiments:
  enum ConsistencyModels:
    case STRICT, SEQUENTIAL, CAUSAL, EVENTUAL

  val cv = ConsistencyModels.values.toList
  println(cv)
  val b = Try(ConsistencyModels.valueOf("SEQUENTIAL1".toUpperCase)) match {
    case Success(res) => res
    case Failure(e) => e.getMessage
  }

  val c = Try(ConsistencyModels.valueOf("EVENTUAL".toUpperCase)) match {
    case Success(res) => res
    case Failure(e) => e.getMessage
  }

  @main def runIt =
    ConsistencyModels.values.foreach(v=>println(v.ordinal))
    println("max value " + ConsistencyModels.values.map(_.ordinal).toList.max)
    println(b)
    println(c)