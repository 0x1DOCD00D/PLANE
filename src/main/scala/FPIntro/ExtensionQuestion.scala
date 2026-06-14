////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package FPIntro

//https://users.scala-lang.org/t/prevent-extensions-from-being-invokes-as-functions/10529
/*
  The extension method `foo` desugars into a regular member of the object with the receiver as a leading parameter, 
  so its signature becomes `(s: String): Int`. A plain application like `foo(s)` collects that alongside the generic 
  `foo[A](any: A): Int`, and both are applicable to a `String` argument. So the two are genuine competing overloads, exactly as the compiler's error reports.

  The reason this errors rather than picking a winner is the most-specific test, which runs pairwise. 
  The generic method accepts a `String` with `A` inferred as `String`, so it is as specific as the concrete one. 
  The concrete method is applicable to the generic's parameter type too, because that parameter type is the type variable `A`, 
  which is solvable here rather than frozen, and it can be instantiated to `String`. Each alternative is as specific as the other, 
  there is no unique maximum, and the compiler reports an ambiguous overload. The lesson is that overloading a generic method with 
  a monomorphic specialization of itself does not reliably prefer the specialization, since a type parameter ties with the very 
  type it could be instantiated to. The program never compiles, so `s.foo` (which on its own would resolve to the extension and yield 2) never runs.
 * */
object ExtensionQuestion:
  def foo[A](any: A): Int = 1

  extension (s: String) def foo: Int = 2

  @main def runExtensionQuestion(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/FPIntro/ExtensionQuestion.scala created at time 4:04PM")
    val s = ""
    /*
     Ambiguous overload. The overloaded alternatives of method foo in object ExtensionQuestion with types
     (s: String): Int
     [A](any: A): Int
     both match arguments ((s : String))
    * */
//    println(foo(s))
    println(s.foo)
