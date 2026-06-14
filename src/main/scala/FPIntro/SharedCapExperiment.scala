////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package FPIntro

//https://users.scala-lang.org/t/capture-checking-being-confusing-again/12083

import scala.language.experimental.captureChecking
import scala.caps.*

object SharedCapExperiment:
  trait State[A] extends SharedCapability:
    def get: A
    def set(a: A): Unit

  def get[A]: State[A] ?-> A = s ?=> s.get
  def set[A](a: A): State[A] ?-> Unit = s ?=> s.set(a)

  // Not a capability class
  trait Rand:
    def range(min: Int, max: Int): Int

  object Rand:
    def fromState: (s: State[Long]) ?-> Rand^{s} =
      s ?=> new Rand:
        override def range(min: Int, max: Int): Int =
          // FIX 1: the expected type Long forces the context function to be
          // applied here, resolving the given State[Long] (which is s).
          // Without the annotation, seed would be inferred as the *function*
          // State[Long] ?-> Long, and `seed + 1` would not type-check.
          val seed: Long = get[Long]
          val nextSeed = seed + 1
          // FIX 2: apply explicitly so the effect actually runs. In bare
          // statement position there's no expected type to trigger application,
          // so the context-function value gets built and discarded instead of
          // run, and the seed would never advance.
          set(nextSeed)(using s)
          seed.toInt

  @main def runSharedCapExperiment(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/FPIntro/SharedCapExperiment.scala created at time 4:27PM")
    val s: State[Long] = new State[Long]:
      private var x = 42L
      def get: Long = x
      def set(a: Long): Unit = x = a

    // Build a Rand that captures the capability s
    val rng: Rand^{s} = Rand.fromState(using s)

    println(rng.range(0, 10)) // 42
    println(rng.range(0, 10)) // 43
    println(rng.range(0, 10)) // 44

    // Inspect the raw state directly via the capability
    val currentSeed: Long = get[Long](using s)
    println(s"raw seed now = $currentSeed") // raw seed now = 45