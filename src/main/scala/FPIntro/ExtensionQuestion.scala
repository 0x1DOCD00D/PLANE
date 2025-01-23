////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package FPIntro

//https://users.scala-lang.org/t/prevent-extensions-from-being-invokes-as-functions/10529
/*
 * In Scala 3, an extension method on a String is compiled as if there is a more specific foo(String) method in scope. Because foo(String) is more specific than the generic fooA, the compiler chooses the extension method when you call foo(s), where s is a String. As a result, both println(foo(s)) and println(s.foo) end up calling the extension method, which returns 2.
 * */
object ExtensionQuestion:
  def foo[A](any: A): Int = 1

  extension (s: String) def foo: Int = 2

  @main def runExtensionQuestion(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/FPIntro/ExtensionQuestion.scala created at time 4:04PM")
    val s = ""
    println(foo(s))
    println(s.foo)
