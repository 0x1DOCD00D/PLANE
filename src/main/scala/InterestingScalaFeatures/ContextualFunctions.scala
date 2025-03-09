////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package InterestingScalaFeatures
/*
  https://users.scala-lang.org/t/type-a-parameter-as-either-a-contextual-function-null/10618/2

  A variable v of type A ?=> B when present in code will always look for a A in given scope and have type B
  When the expected type is C ?=> D, the expression e is automatically preceded by (using C) => e

Example:

val zeroth: String ?=> Int = 4 // desugars to (s: String) ?=> 4
val first = (String) ?=> 4
val second = first // Looks for String in scope, and returns it, second has type Int
val third: String ?=> Int = first // desugars to the following:
// (s: String) ?=> first(using s)
 * */

object ContextualFunctions:
  given ctxString: String = "Hello from given context!"
  given Int = 100

//  vanilla use
  val greet: String ?=> String = summon[String] + ", user!"
  val feed: String ?=> Int = summon[String].length
  val multiplyByFactor: Int ?=> Int = summon[Int] * 2
  def useGreet(using String): String = greet
  def useMultiplier(using Int): Int = multiplyByFactor

//  turning up the heat
  def f(a: String, g: Option[(x: Int) ?=> Int] = None) = g match
    case Some(p) => p.toString
    case None    => a

  val nullableFunction1: (Int ?=> Int) | Null = null
  val nullableFunction2: (Int ?=> Int) | Null = null

  /*def applyContextual[A, B](f: (A ?=> B) | Null)(using A): Option[B] =
    f match
      case null => None
      case fCtx => Some(fCtx(using summon[A]))*/
  def applyContextual[A, B](f: (A ?=> B) | Null)(using A): Option[B] =
    Option(f.asInstanceOf[Option[A ?=> B]]).flatten.map(fCtx => fCtx(using summon[A]))

  @main def runContextualFunctions(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/InterestingScalaFeatures/ContextualFunctions.scala created at time 11:59AM")
    given Int = 5
    println(f("test f", Some(x + 5)))
    println(greet)
    println(feed)
    println(multiplyByFactor)
    println(useGreet)
    println(useMultiplier)

    println(applyContextual(nullableFunction1))
