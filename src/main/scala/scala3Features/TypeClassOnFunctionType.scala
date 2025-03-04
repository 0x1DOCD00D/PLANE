
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scala3Features

//https://users.scala-lang.org/t/type-class-on-a-function-type/10600/3

object TypeClassOnFunctionType:
  trait AttemptComposer[A] {
    extension (t1: A) infix def orElse(t2: A): A
  }

  case class Tag[A](
                     result: Option[A],
                     writer: Seq[Map[String, Any]]
                   )

  object Tag {
    type Something = Map[String, Any]
    type FunctionTag = Something => Tag[Int]

    implicit val attemptComposerFunctionTag: AttemptComposer[FunctionTag] = new AttemptComposer[FunctionTag] {
      extension (fT1: FunctionTag) def orElse(fT2: FunctionTag): FunctionTag = fT1
    }
  }

  import Tag.{Something, FunctionTag}

  def cTag1(s: Something): Tag[Int] = Tag(None, Seq(Map.empty))

  def cTag2(s: Something): Tag[Int] = Tag(Some(3), Seq(Map.empty))
  
  @main def runTypeClassOnFunctionType(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/scala3Features/TypeClassOnFunctionType.scala created at time 7:44PM")

    val cTag3 = cTag1 orElse cTag2
