////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scala3Features.NewTypes

//https://users.scala-lang.org/t/desugaring-of-for-with-pattern-match-and-if-guard/12147

object PatternMatchWithIfGuard:
  trait A {
    def a: Boolean
  }

  trait B extends A {
    def b: Boolean
  }

  def f(): Seq[B] = {
    val s = Seq[A]()

    val as = for (case x: A <- s if x.a) yield x // this compiles, the result is Seq[A]
    val bs = for (case x: B <- s) yield x // this compiles, the result is Seq[B]
    for (case x: B <- s) yield x.b // this compiles
//    for (case x: B <- s if x.b) yield x // this gives an error: Found B => B, Required A => B
    s.collect { case x: B if x.b => x }
    (for (case x: B <- s) yield x).filter(_.b)
  }

  @main def runPatternMatchWithIfGuard(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/scala3Features/NewTypes/PatternMatchWithIfGuard.scala created at time 5:43PM")
    f()
