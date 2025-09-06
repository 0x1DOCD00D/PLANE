
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats

object EvalMonadAlwaysComprehension:

  import cats.Eval
  import scala.util.Random

  val randomEval: Eval[Int] = Eval.always(Random.nextInt(100))

  val program: Eval[String] =
    for
      a <- randomEval
      b <- randomEval
    yield s"Two random numbers: $a and $b"


  @main def runEvalMonadAlwaysComprehension(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Cats/EvalMonadAlwaysComprehension.scala created at time 3:12PM")
    println(program.value) // each bind recomputes, both are different
    println(program.value) // recomputes again, fresh values
