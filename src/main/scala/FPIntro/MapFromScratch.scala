
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package FPIntro

import DSLWorkshop.BuilderDsl.ListBuilder

import scala.collection.mutable.ListBuffer

object MapFromScratch:
  def mymap[From, To](lst: List[From])(f: From => To): List[To] =
    val len = lst.length
    var index = 0
    val dest = ListBuffer[To]()
    while(index < len) {
      dest += f(lst(index))
      index = index+1
    }
    dest.toList
  
  @main def runMapFromScratch(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/FPIntro/MapFromScratch.scala created at time 2:18PM")
    println(mymap(List("aaa", "bbbbb"))((s:String)=>s.length))
