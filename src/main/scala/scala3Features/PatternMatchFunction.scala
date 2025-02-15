
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scala3Features

//https://users.scala-lang.org/t/matching-on-a-context-function/10562
//matching on a context function

import scala.concurrent.Future

object PatternMatchFunction:
  transparent inline def eval[A, B](f: A ?=> B, x: A): Any =
    val f0 = a => f(using a)
    inline f0 match
      case g: (A => Future[?]) => g(x).value.get.get
      case _ => f0(x)

  
  @main def runPatternMatchFunction(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/scala3Features/PatternMatchFunction.scala created at time 5:51PM")
    val res1 = eval(Future.successful(summon[Int]), 10)
    val res2 = eval(summon[Int] + 1, 10)
    println(s"$res1, $res2")

