
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package TypeFP.PhantomTypes

object SwitchPT:
  // Based on "Functional Programming Strategies In Scala with Cats" Chapter 13
  // Examples of Phantom Types starting with the SwitchPT example

  // First, let's define our phantom type tags for SwitchPT states
  sealed trait SwitchState

  sealed trait On extends SwitchState

  sealed trait Off extends SwitchState

  // The SwitchPT example using phantom types to track state at compile time
  final case class Switch[State <: SwitchState] private(isOn: Boolean)

  object Switch {
    // Constructor for creating a new switch in the Off state
    def create: Switch[Off] = Switch[Off](false)

    // Methods that change state are only available for appropriate states
    extension [S <: On](switch: Switch[S]) {
      def turnOff: Switch[Off] = Switch[Off](false)
    }

    extension [S <: Off](switch: Switch[S]) {
      def turnOn: Switch[On] = Switch[On](true)
    }

    // Method available regardless of state
    extension [S <: SwitchState](switch: Switch[S]) {
      def isOn: Boolean = switch.isOn
    }
  }

  @main def runSwitch(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/TypeFP/PhantomTypes/SwitchPT.scala created at time 11:37AM")
    val wallSwitch = Switch.create
    wallSwitch.turnOn.turnOff
    wallSwitch.turnOn.turnOff.turnOn

    val switch = Switch.create // SwitchPT[Off]

    //switch.turnOff // This would not compile! SwitchPT is already off

    val onSwitch = switch.turnOn // SwitchPT[On]
    println(s"SwitchPT is on: ${onSwitch.isOn}")

    val offSwitch = onSwitch.turnOff // SwitchPT[Off]
    println(s"SwitchPT is on: ${offSwitch.isOn}")

    onSwitch.turnOff

