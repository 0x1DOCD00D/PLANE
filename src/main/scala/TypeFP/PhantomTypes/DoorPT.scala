
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package TypeFP.PhantomTypes

object DoorPT:
  // More sophisticated example: Door with multiple states
  sealed trait DoorState

  sealed trait Closed extends DoorState

  sealed trait Open extends DoorState

  sealed trait Locked extends DoorState

  final case class Door[State <: DoorState] private(name: String)

  object Door {
    def create(name: String): Door[Closed] = Door[Closed](name)

    extension [S <: Closed](door: Door[S]) {
      def open: Door[Open] = Door[Open](door.name)

      def lock: Door[Locked] = Door[Locked](door.name)
    }

    extension [S <: Open](door: Door[S]) {
      def close: Door[Closed] = Door[Closed](door.name)
    }

    extension [S <: Locked](door: Door[S]) {
      def unlock: Door[Closed] = Door[Closed](door.name)
    }
  }


  @main def runDoorPT(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/TypeFP/PhantomTypes/DoorPT.scala created at time 11:44AM")
    import Door.*
    val d1: Door[Closed] = Door.create("Front")
    val d2: Door[Open] = d1.open
    val d3: Door[Closed] = d2.close
    val d4: Door[Locked] = d3.lock
    val d5: Door[Closed] = d4.unlock
    println(s"Doors: $d1, $d2, $d3, $d4, $d5")
    // The following lines would not compile:
    //val d6: Door[Open] = d1.close
    // val d7: Door[Closed] = d2.lock
    // val d8: Door[Locked] = d3.unlock
    // val d9: Door[Open] = d4.open
