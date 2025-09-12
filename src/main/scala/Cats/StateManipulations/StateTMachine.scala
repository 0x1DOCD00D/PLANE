
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.StateManipulations

import cats.data.{State, StateT}
import cats.effect.{IO, IOApp}
import cats.syntax.all.*

//https://typelevel.org/cats/datatypes/statet.html

// Our machine: just a counter
final case class Machine(counter: Int)

// Type alias for convenience
type MachineState[A] = StateT[IO, Machine, A]

object StateTExample extends IOApp.Simple {

  // A program that increments the counter and returns the old value
  val increment: MachineState[Int] = StateT { machine =>
    IO {
      val oldValue = machine.counter
      val newMachine = machine.copy(counter = oldValue + 1)
      (newMachine, oldValue) // StateT wants (new state, result)
    }
  }

  // A program that multiplies the counter and returns the new value
  def multiplyBy(n: Int): MachineState[Int] = StateT { machine =>
    IO {
      val newMachine = machine.copy(counter = machine.counter * n)
      (newMachine, newMachine.counter)
    }
  }

  // A composed program using for-comprehension
  val program: MachineState[String] = for {
    old <- increment // old counter
    _ = println(old)
    nowInc <- StateT.get[IO, Machine].map(_.counter)
    _ = println(s"Current counter after increment: $nowInc")
    now <- multiplyBy(5) // new counter
  } yield s"Started at $old, now at $now"

  // Run the whole thing
  def run: IO[Unit] =
    program.run(Machine(10)).flatMap { case (finalMachine, result) =>
      IO.println(s"Final machine: $finalMachine, result: $result")
    }
}
