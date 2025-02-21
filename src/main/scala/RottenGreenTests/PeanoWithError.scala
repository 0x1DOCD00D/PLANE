////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package RottenGreenTests

object PeanoWithError:
  sealed trait Peano {
    def add(that: Peano): Peano
  }

  case object Zero extends Peano {
    def add(that: Peano): Peano = that
  }

  case class Succ[N <: Peano](n: N) extends Peano {
    def add(that: Peano): Peano = that match {
      case Zero => this
      case Succ(m) => if (this == Zero) m else Succ(n.add(m))
    }
  }

  @main def runPeanoWithError(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/RottenGreenTests/PeanoWithError.scala created at time 1:33PM")
    val x = Succ(Succ(Zero)) // Peano(2)
    val y = Succ(Succ(Succ(Zero))) // Peano(3)
    val z = Succ(Zero) // Peano(1), now a real test

    val left = x.add(y).add(z) // (2 + 3) + 1
    val right = x.add(y.add(z)) // 2 + (3 + 1)

    println(s"left: $left, right: $right") // left: Succ(Succ(Succ(Zero))), right: Succ(Succ(Succ(Zero)))
    println(left == right) // Test incorrectly passes due to hidden bug!
