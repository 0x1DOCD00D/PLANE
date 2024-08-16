/*
 * Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package TypeFP

object DependentTypes1:
  def dependentlyTypedMod2[N <: NatT](n: N): Mod2[N] = n match
    case _: Zero.type => Zero
    case s: Succ[?] => s match
      case Succ(predN) => predN match
        case _: Zero.type => Succ(Zero)
        case s: Succ[?] => s match
          case Succ(predPredN) => dependentlyTypedMod2(predPredN)

  type Mod2[N <: NatT] <: NatT = N match
    case Zero.type => Zero.type 
    case Succ[predN] => predN match
      case Zero.type => Succ[Zero.type]
      case Succ[predPredN] => Mod2[predPredN]

  sealed trait NatT

  case object Zero extends NatT

  case class Succ[N <: NatT](n: N) extends NatT
  @main def runDependentTypes1():Unit =
    dependentlyTypedMod2(Succ(Succ(Succ(Zero))) )
    println("Dependent types are types that depend on values.")