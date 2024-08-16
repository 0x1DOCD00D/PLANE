/*
 * Copyright (c) 2023 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package FPIntro

import scala.annotation.tailrec

object ProgressCounterInMap:
  final def reportProgress(msg: String, total: Int, tenPercent: Int)(progress: Int = 0): Unit =
    if progress >= total then
      println("Done!")
    else
      if progress % tenPercent == 0 then
        println(s"$msg: ${progress * 100 / total}%")
    ()
  end reportProgress
  @main def runProgressCounterInMap(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/FPIntro/ProgressCounterInMap.scala created at time 1:04 PM")
    val rp = reportProgress("Reading", 100, 10)
    val list = (1 to 100).toList.zipWithIndex.map {
      (e,i) =>
        rp(i)
        e+1
    }
