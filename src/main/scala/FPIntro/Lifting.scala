
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package FPIntro

object Lifting:
  def lift[I, O](f: PartialFunction[I, O]): Function[I,Option[O]] = f.lift 
  
  @main def runLifting(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/FPIntro/Lifting.scala created at time 1:57PM")
    val f: PartialFunction[String, Int] = new PartialFunction[String, Int]:
      override def isDefinedAt(x: String): Boolean = if x.count(_.isDigit) != x.length then false else true
      override def apply(v1: String): Int = v1.toInt
    
    println(lift(f)("a11"))
    println(lift(f)("112"))
