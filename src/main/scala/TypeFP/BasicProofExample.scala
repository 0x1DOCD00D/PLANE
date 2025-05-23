
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package TypeFP

import scala.util.NotGiven

object BasicProofExample:
  trait A; trait B; trait C
  given proofAB(using a: A): B with
    new B {}

  given vacuous(
    using NotGiven[A] ): B with
    new B {}
  given A with {} //this proposition can be commented out and the proof still works vacuously

  @main def runBasicProofExample(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/TypeFP/BasicProofExample.scala created at time 4:22PM")
    def use[T](v: T): Unit = println(s"Using ${v.getClass.getName}")
    use(summon[B])
    //if we comment out given A with {} the proof still works vacuously
//    this theorem is not provable, uncomment it for a compile-time error
//    use(summon[C])

