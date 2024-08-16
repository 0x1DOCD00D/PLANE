/*
 * Copyright (c) 2023 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Language.CollectionsGame

import scala.util.Try

object MapsStuff:
  val someMap: Map[String, Int] = Map("a" -> 0, "b" -> 0, "c" -> 0)
  val lstOfNames: List[String] = List("a", "b", "c", "d", "e", "f", "g", "h", "a", "b", "c", "d", "a", "b", "c")

  def updateMapCount(): Map[String, Int] =
    lstOfNames.foldLeft(someMap) { (map, name) =>
      println(s"map = $map, name = $name")
      map + (name ->
        (
          Try(map(name)).getOrElse(0) + 1)
        )
    }
  @main def runMapsStuff(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Language/CollectionsGame/MapsStuff.scala created at time 1:29 PM")
    println(updateMapCount())