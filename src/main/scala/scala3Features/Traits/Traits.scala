
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scala3Features.Traits

object Traits:
  trait Person
  class Base(val v: String) {
    def this(arg1: Int) = this((arg1+1).toString)
    def this(arg2: Person) = this(arg2.toString)
    def this(arg3: Double) = this(arg3.toString)
  }

  trait Extended extends Base:
    override def toString: String = "Extended: " + v

  object Extended {
    def apply(arg1: Int) = new Base(arg1) with Extended

    def apply(arg2: Person) = new Base(arg2) with Extended

    def apply(arg3: Double) = new Base(arg3) with Extended
  }
  @main def runTraits(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/scala3Features/Traits.scala created at time 8:39PM")
    println(Extended(1).toString)
