////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Givens

import Givens.InductiveTypeNatWithGiven.IsEven.proveEven

object InductiveTypeNatWithGiven:
  sealed trait Nat

  case object Zero extends Nat

  case class Succ[N <: Nat](prev: N) extends Nat

  trait IsEven[N <: Nat]

  object IsEven:
    given isEvenZero: IsEven[Zero.type] = null // new IsEven[Zero.type] {}

    given isEvenSucc[N <: Nat](using IsEven[N]): IsEven[Succ[Succ[N]]] = null // new IsEven[Succ[Succ[N]]] {}

    def proveEven[N <: Nat](using ev: IsEven[N]): Unit = ()

  @main def runInductiveTypeNatWithGiven(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Givens/InductiveTypeNatWithGiven.scala created at time 3:06PM")
    type Two = Succ[Succ[Zero.type]]
    type Three = Succ[Two]
    type Four = Succ[Succ[Two]]
    type Seven = Succ[Succ[Succ[Four]]]
    type Ten = Succ[Succ[Succ[Seven]]]

    println(proveEven[Zero.type])
    println(proveEven[Two])
    println(proveEven[Four])
    println(proveEven[Ten])

/*
    println(proveEven[Succ[Zero.type]])
    println(proveEven[Three])
 */
