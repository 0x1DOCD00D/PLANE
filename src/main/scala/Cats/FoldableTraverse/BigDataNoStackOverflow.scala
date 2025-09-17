
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.FoldableTraverse

object BigDataNoStackOverflow:

  import cats.* 
  import cats.implicits.* 
  import cats.Eval

  val big: LazyList[Long] = LazyList.from(1).map(_.toLong).take(100000)

  // Stack-safe folding (uses Eval internally via Cats’ Foldable[Stream])
  val safeSum: Long = Foldable[LazyList].foldRight(big, Eval.now(0L))((n, acc) => acc.map(_ + n)).value

  @main def runBigDataNoStackOverflow(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Cats/FoldableTraverse/BigDataNoStackOverflow.scala created at time 2:12PM")
    println(s"Sum of first 100,000 integers is $safeSum")
