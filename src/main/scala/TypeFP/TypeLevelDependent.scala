////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package TypeFP

import scala.compiletime.ops.int.*

object TypeLevelDependent:
  object ValueLevelComputation {
    def applyFunc[F <: Int => Int](f: F, x: Int): Int = f(x)
    def Triple(x: Int): Int = x * 3
    val computed: Int = applyFunc(Triple, 5) // Should be 10
  }

  object TypeLevelComputation {
    type ApplyFunc[F[_ <: Int] <: Int, X <: Int] = F[X]
    type TripleF[X <: Int] = X match {
      case 0 => 0
      case _ => X * 3
    }
    summon[ApplyFunc[TripleF, 5] =:= 15]
  }

  trait ResultContainer[F[_ <: Int], X <: Int] {
    type Computed <: Int
  }

  type ApplyFunc[F[_ <: Int] <: Int, X <: Int] =
    ResultContainer[F, X] {
      type Computed = F[X]
    }

  type TripleF[X <: Int] <: Int = X match {
    case 0 => 0
    case _ => X * 3
  }

  type ExtractResult[T] = T match {
    case ResultContainer[f, x] => f[x]
  }

  summon[ExtractResult[ApplyFunc[TripleF, 5]] =:= 15]
//  summon[FinalResult =:= 20]
  @main def runTypeLevelDependent(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/TypeFP/TypeLevelDependent.scala created at time 12:53PM")
    println(ValueLevelComputation.computed)
