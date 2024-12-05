
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package FPIntro

object TraitWithExtension:
  trait C[A]:
    def ofString(s: String): A

    def stringOf(a: A): String

    extension (a: A) 
      def m: String = stringOf(a)

  def code[A: C]: String =
    import scala.language.implicitConversions
    given Conversion[String, A] with
      def apply(s: String): A = summon[C[A]].ofString(s)
    val ev = summon[C[A]]
    import ev.m
    "Y".m

  @main def runTraitWithExtension(args: String*): Unit =
    import scala.language.implicitConversions
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/FPIntro/TraitWithExtension.scala created at time 11:14AM")
//    println(code)
