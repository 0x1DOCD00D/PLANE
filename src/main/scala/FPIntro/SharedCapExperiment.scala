////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package FPIntro

//https://users.scala-lang.org/t/capture-checking-being-confusing-again/12083

import language.experimental.captureChecking

object SharedCapExperiment:
/*
 as of 10/30/25 this file uses experimental Scala 3 capture checking features, but this project's compiler
 isn't configured to handle them. When the compiler tries to process this file, it crashes with a StackOverflowError.
 Since capture checking was intentionally removed from your build configuration, the solution is to remove this file entirely.
* */

/*
  import caps.*

  trait State[A] extends SharedCapability:
    def get: A

    def set(a: A): Unit

  def get[A]: State[A] ?-> A = s ?=> s.get

  def set[A](a: A): State[A] ?-> Unit = s ?=> s.set(a)

  trait Rand extends SharedCapability:
    def range(min: Int, max: Int): Int

  object rand:
    def fromState: (s: State[Long]) ?-> Rand ^ {s} =
      new Rand:
        override def range(min: Int, max: Int) =
          val seed = get
          val (nextSeed, next) = (seed + 1, seed.toInt)
          set(nextSeed)
          next
*/

  @main def runSharedCapExperiment(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/FPIntro/SharedCapExperiment.scala created at time 4:27PM")
