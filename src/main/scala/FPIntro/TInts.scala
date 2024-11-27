
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package FPIntro

//https://users.scala-lang.org/t/how-to-fix-these-tagged-ints/10403

object TInts:
  trait SomeRandomType
  opaque type TInt[+T] = Int

  object TInt:
    inline def apply[T](value: Int): TInt[T] = value
    given [T]: Conversion[TInt[T], Int] with
      def apply(n: TInt[T]): Int = n

  extension [T](m: TInt[T])
    inline def +[I](n: I)(using Conversion[I, Int]): TInt[T] | I =
      import scala.language.implicitConversions
      (m: Int) + (n: Int)

  @main def runTInts(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/FPIntro/TInts.scala created at time 4:33PM")
    val a: TInt[Double] = TInt(3)
    val b: TInt[String] = 2
    val res: TInt[SomeRandomType] | Int = a + b
    println(res)