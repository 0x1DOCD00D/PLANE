
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package FPIntro

//https://users.scala-lang.org/t/are-warnings-ok-when-matching-on-generic-types/10397

object MatchOnGenerics:
  sealed trait DiffResult extends Product with Serializable

  case class DiffResultAdditional[T](value: T) extends DiffResult

  case class Foo()

  @main def runMatchOnGenerics(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/FPIntro/MatchOnGenerics.scala created at time 5:09PM")
    val diffResult: DiffResult = DiffResultAdditional(Foo())

    import compiletime.asMatchable
    diffResult match
      case DiffResultAdditional(foo) =>
        println("DiffResultAdditional")
        foo.asMatchable match
          case _: Foo => println("foo")
          case _ => println("whatever!")
      case _ => println("ouch!")   
