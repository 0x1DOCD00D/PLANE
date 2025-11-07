////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package ReflectionExperiments

import java.sql.SQLException

//https://users.scala-lang.org/t/typetest-on-throw-exception/11998/5
object TypeTestException:

  import scala.reflect.TypeTest
  import java.io.IOException

  def b: Boolean = true

  def catchItWithoutTypeTest [E](thunk: => Unit): Unit =
    try thunk
    catch case e: E => println("catch it")

  def catchItWithTypeTest[E1, E2](value: E1)(using tt: TypeTest[E1, E2]): Unit =
    value match
      case e: E2 => println(s"matched E2: $e") // Uses TypeTest[E1, E2]!
      case _ => println("didn't match")

  def test_exception[E <: Exception](thunk: => Unit): Unit =
    try
      thunk
    catch
      case e: E => println("te:1")
      case _ => println("whatever")

  private def test_exception2[E](thunk: => Unit)(using TypeTest[Exception, E]): Unit =
    val e: Exception = NullPointerException()
    e match
      case _: E => println("ok")
      case _ => println("wut")
    try
      thunk
      //catch case e: E => throw ArrayIndexOutOfBoundsException()
    catch case e: E if b => println("te:2")

  @main def runTypeTestException(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/ReflectionExperiments/TypeTestException.scala created at time 10:37AM")
    test_exception[NullPointerException]:
      throw null
    test_exception2[String]:
      //throw Exception()
      throw null
    catchItWithoutTypeTest[ArrayIndexOutOfBoundsException]:
      throw SQLException()

    catchItWithTypeTest[String, SQLException]:
      throw IOException()