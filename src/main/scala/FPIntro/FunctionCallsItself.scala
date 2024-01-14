/*
 * Copyright (c) 2023 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package FPIntro

object FunctionCallsItself:
  implicit class ReadFunction(f: Int => ReadFunction) extends (Int => ReadFunction) {
    def apply(i: Int) = f(i)
  }

  lazy val read: ReadFunction = { (i: Int) => println(i); read }
/*

  class SomeFunction extends (Int => SomeFunction):
    def apply(i: Int) = this


  given Conversion[Int=>SomeFunction, Int=>SomeFunction] with
    def apply(f: Int=>SomeFunction): Int => SomeFunction = new SomeFunction {
      def apply(i: Int): SomeFunction = f
    }
*/

  @main def runFunctionCallsItself(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/FPIntro/FunctionCallsItself.scala created at time 2:01 PM")
    println(read(2)(3))
