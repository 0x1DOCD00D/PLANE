
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scala3Features.Context

//https://users.scala-lang.org/t/extension-method-resolution-is-this-expected-behaviour/12126

object ExtensionMethodResolution:
  /*
  trait A[TA]:
    extension (a1: TA) def *(a2: TA): TA

  trait B[TA: A, TB]:
    extension (a: TA) def *(b: TB): TB

  def breaks[TA: A, TB: [TB] =>> B[TA, TB]](a: TA): TA =

    Found:    (a : TA)
Required: ?{ * : ? }
Note that implicit extension methods cannot be applied because they are ambiguous;
both parameter evidence$2 and parameter evidence$1 provide an extension method `*` on (a : TA)

Explanation
===========

Tree:

  a

I tried to show that
  (a : TA)
conforms to
  ?{ * : ? }
but none of the attempts shown below succeeded:

  ==> TermRef(NoPrefix,val a)  <:  SelectionProto(*,class dotty.tools.dotc.typer.ProtoTypes$CachedIgnoredProto,dotty.tools.dotc.typer.Typer@3256eab3,true,[1423..1424])  = false

    a * a
*/

  // Trait definitions (using renamed method to fix the ambiguity)
  trait A[TA]:
    extension (a1: TA) def *(a2: TA): TA

  trait B[TA: A, TB]:
    extension (a: TA) def x(b: TB): TB

  // Concrete implementations
  given A[Int] with
    extension (a1: Int) def *(a2: Int): Int = a1 + a2

  given A[String] with
    extension (a1: String) def *(a2: String): String = a1 + a2

  given B[Int, String] with
    extension (a: Int)
      def x(b: String): String = (1 to a).foldLeft("")((acc, _) => acc + b)

  given B[String, Int] with
    extension (a: String) def x(b: Int): Int = a.length * b

  // The fixed function
  def breaks[TA: A, TB: [TBx] =>> B[TA, TBx]](a: TA, a2: TA, b: TB): TB =
    val combined = a * a2 // Uses A's * method
    combined x b // Uses B's x method

  @main def runExtensionMethodResolution(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/scala3Features/Context/ExtensionMethodResolution.scala created at time 11:30AM")

    val result1: String = breaks[Int, String](3, 5, "Hello")
    // 3 * 5 = 8 (using A[Int]), then 8 x "Hello" = "HelloHelloHelloHelloHelloHelloHelloHello"
    println(result1)

    val result2: Int = breaks[String, Int]("Hi", "There", 10)
    // "Hi" * "There" = "HiThere" (using A[String]), then "HiThere".length * 10 = 70
    println(result2)

    val result3 = breaks(3, 5, "World") // Scala infers [Int, String]
    println(result3)

