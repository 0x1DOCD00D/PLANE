////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package FPIntro

import FPIntro.Match4UnionTypes.U.{I, S}
import FPIntro.Match4UnionTypes.UnionSI.{uI, uS}
import scala.reflect.TypeTest

//https://users.scala-lang.org/t/exhaustive-match-for-union-type/12092

/*
TypeTest is a mechanism in Scala 3 that gets invoked during type pattern matching when the compiler
needs runtime verification of types that cannot be reliably checked through standard JVM type erasure.

When you write a pattern match with a type test like case x: SomeType =>, the compiler looks for a given TypeTest instance.
For example:
    x match { case s: uS => ... } - If uS has a TypeTest, it will be invoked
    x match { case i: uI => ... } - If uI has a TypeTest, it will be invoked

When you use .isInstanceOf[T] and T has a TypeTest defined, it may be invoked instead of the standard Java instanceof check.

Type Ascriptions with Pattern Matching: when casting or checking against types that have type erasure issues (like opaque types, refined types, or complex type bounds).
Why TypeTest Exists
The JVM erases most generic type information at runtime. TypeTest provides a way to
    Check opaque types (which don't exist at runtime)
    Check types with erased generics (like List[Int] vs List[String])
    Implement custom type checking logic
    Work around JVM type erasure limitations

  First call to uI.unapply(S("hello")) - checking if S("hello") is of type uI
  Inside that unapply, it tries to match against uI and uS
  The match against uS triggers another TypeTest lookup
  Neither case matches, and there's no default case → MatchError

TypeTest has this signature: trait TypeTest[-S, T] with a single method def unapply(s: S): Option[T & s.type]
When the Scala compiler encounters a type pattern like case x: MyType => it
  Searches for a given TypeTest[SourceType, MyType] in scope
  If found, calls the unapply method on that TypeTest instance
  If unapply returns Some(value), the pattern matches
  If unapply returns None, the pattern fails and tries the next case

The bug occurs because inside the uI TypeTest's unapply method (line 42-49), there's a nested pattern match that itself tries to use type patterns case i: uI and case s: uS. This creates a recursive TypeTest invocation - the TypeTest for uI calls another TypeTest for uS, and when passed an enum case S("hello") (which is neither an Int nor a String at runtime), the match fails with no default case.
The opaque types uI and uI are just compile-time aliases for Int and String, but at runtime, the enum cases S(String) and I(Int) are their own distinct classes: Match4UnionTypes$U$S and Match4UnionTypes$U$I.
So when you try to match an enum case against these opaque types, the TypeTest fails because the runtime class doesn't match.
* * */
object Match4UnionTypes:
  enum U:
    case S(s: String)
    case I(i: Int)

  type UnionSI = S | I

  object UnionSI {

    opaque type uS = String
    opaque type uI = Int

    object uS:
      inline def apply(inline s: uS): uS = s

      inline def unapply(inline s: uS): Some[String] =
        println(s"unapply uS inline: ${s.getClass}")
        Some(s)

      given TypeTest[Any, uS] with
        def unapply(x: Any): Option[uS & x.type] =
          println(s"unapply uS: ${x.getClass}")
          x match
            case s: String => Some(s.asInstanceOf[uS & x.type])
            case i: Int         => None

    object uI:
      inline def apply(inline i: uI): uI =
        println(s"apply uI inline: ${i.getClass}")
        i

      inline def unapply(inline i: uI): Some[Int] = Some(i)

      given TypeTest[Any, uI] with
        def unapply(x: Any): Option[uI & x.type] =
          println(s"unapply uI: ${x.getClass}")
          x match
            case i: uI => Some(i.asInstanceOf[uI & x.type])
            case s: uS    => None
  }

  @main def runMatch4UnionTypes(args: String*): Unit =
    println(
       "File /Users/drmark/IdeaProjects/PLANE/src/main/scala/FPIntro/Match4UnionTypes.scala created at time 1:12PM"
    )
    val u: UnionSI = S("hello")
    val v: UnionSI = I(123)
    u match
      case S(s) => println(s)
      case I(i) => println(i)

    v match
      case S(s) => println(s)
      case I(i) => println(i)

    u match
      case us: uS => println(s"Matched uS: $us")
      case ui: uI => println(s"Matched uI: $ui")
      case _ => println("No match")

