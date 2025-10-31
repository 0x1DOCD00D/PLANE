////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package FPIntro

//https://users.scala-lang.org/t/capture-checking-being-confusing-again/12083

/*
 as of 10/30/25 this file uses experimental Scala 3 capture checking features, but this project's compiler
 isn't configured to handle them. When the compiler tries to process this file, it crashes with a StackOverflowError.
 Since capture checking was intentionally removed from your build configuration, the solution is to remove this file entirely.
import scala.language.experimental.captureChecking
import scala.caps.*

object SharedCapExperiment:
  trait State[A] extends SharedCapability:
    def get: A
    def set(a: A): Unit

  def get[A]: State[A] ?-> A = s ?=> s.get
  def set[A](a: A): State[A] ?-> Unit = s ?=> s.set(a)

  trait Rand extends SharedCapability:
    def range(min: Int, max: Int): Int

  object rand:
    // note: make the capability `s` available with `s ?=>`
    def fromState: (s: State[Long]) ?-> Rand^{s} =
      s ?=> new Rand:
        override def range(min: Int, max: Int) =
          val seed = get[Long]
          val nextSeed = seed + 1
          set(nextSeed)
          // trivial example: ignore bounds for now
          seed.toInt

* */
import scala.language.experimental.captureChecking
import scala.caps.*

object SharedCapExperiment:
  trait State[A] extends Sharable:
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
          val seed = get[Long]
          val nextSeed = seed + 1
          set(nextSeed)
          seed.toInt


  @main def runSharedCapExperiment(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/FPIntro/SharedCapExperiment.scala created at time 4:27PM")
    val s: State[Long] = new State[Long]:
      private var x = 42L

      def get: Long = x

      def set(a: Long): Unit = x = a

    // Build a Rand that captures the capability s
    val rng: Rand^{s} = Rand.fromState(using s) // type inferred: Rand^{s}

    // Use it a few times
    println(rng.range(0, 10)) // e.g. 2  (42 % 10)
    println(rng.range(0, 10)) // e.g. 3  (43 % 10)
    println(rng.range(0, 10)) // e.g. 4  (44 % 10)

    // You can also inspect the raw state directly via the capability
    val currentSeed = get[Long](using s)
    println(s"raw seed now = $currentSeed") // should be 45 here

