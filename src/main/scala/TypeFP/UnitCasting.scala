
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package TypeFP

object UnitCasting:
  def m(s:String, i:Int): PartialFunction[Any, Unit] =
    case _ => (s, i)

  @main def runUnitCasting(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/TypeFP/UnitCasting.scala created at time 8:13PM")
    val r = m("Mark", 58)
    val d1:Unit = r(1)
    val d2:Unit = r("Howdy")

    println {
      d1.asInstanceOf[(String,Int)]._1
      + ", "
      + d2.asInstanceOf[(String,Int)]._2.toString
    }
