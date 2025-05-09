////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scalaMeta

object MetaHelloWorld:
  import scala.meta.*
  val program = """object Main extends App { print("Hello!") }"""
  val tree = program.parse[Source].get

  @main def runMetaHelloWorld(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/scalaMeta/MetaHelloWorld.scala created at time 12:29PM")
    println(tree.show)
