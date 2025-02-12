
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scala3Features

import scala.annotation.unused

object ParamsUntupling:
  def g(f: ((Int, Int)) => Unit): Unit = ()

  g({ case (x, y) => () })
  g((x, y) => ()) // parameter untupling

  def g2(x: (Int, Int)): Unit = ()

  g2((1, 2))
  g2(1, 2)

//  type ArgTypes[S <: String] <: Tuple

  import scala.compiletime.ops.int.+
  import scala.compiletime.ops.string.{CharAt, Length, Substring}
  import scala.Tuple._

  type ArgTypes[S <: String] <: Tuple =
    // First match on the length at the type level:
    Length[S] match
      // If length is 0 => empty string => no arguments
      case 0 => EmptyTuple

      // If length is 1 => we skip this single leftover character
      case 1 => ArgTypes[Substring[S, 1, 1]]

      // If length is at least 2 => inspect the first two characters
      case _ => CharAt[S, 0] match
        case '%' => CharAt[S, 1] match
          case 'd' => Int *: ArgTypes[Substring[S, 2, Length[S]]]
          case 's' => String *: ArgTypes[Substring[S, 2, Length[S]]]
          case _ => ArgTypes[Substring[S, 1, Length[S]]]
        case _ => ArgTypes[Substring[S, 1, Length[S]]]


  def printf(s: String)(t: ArgTypes[s.type]): Unit = ()

  def test() =
    printf("%s is %d")("Ada", 36)
    printf("%d is %s")(12, "Ada")
//    printf("%d is %d")("Ada", 36) //fails
    summon[ArgTypes["%s is %d"] =:= (String, Int)]

  {
    def g(f: ((Int, Int)) => Unit): Unit = ()

    g({
      case (x, y) => ()
    })
    g((x, y) => ())
  }
  @main def runParamsUntupling(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/scala3Features/ParamsUntupling.scala created at time 7:11PM")
