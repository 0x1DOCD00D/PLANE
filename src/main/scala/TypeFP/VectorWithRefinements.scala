
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package TypeFP

object VectorWithRefinements:
  abstract class Vec:
    val size: Int

  val v: Vec {val size: 2} = new Vec:
    val size: 2 = 2

  val vSize: 2 = v.size

  /*
  Found:    (4 : Int)
  Required: (2 : Int) + (2 : Int)
  
  Explanation
  ===========
  
  Tree: 4
  I tried to show that
    (4 : Int)
  conforms to
    (2 : Int) + (2 : Int)
  */
//  infix type +[X <: Int, Y <: Int] <: Int

  import scala.compiletime.ops.int.+

  val a: 2 + 2 = 4

  type IsEmpty[S <: String] <: Boolean = S match {
    case "" => true
    case _ => false
  }

  summon[IsEmpty[""] =:= true]
  summon[IsEmpty["hello"] =:= false]


  abstract class Vec2:
    val size: Int
    
  val v2: Vec2 {val size: 2} = new Vec2:
    val size: 2 = 2
  
  val vSize2: 2 = v2.size
  
  @main def runVectorWithRefinements(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/TypeFP/VectorWithRefinements.scala created at time 7:01PM")
