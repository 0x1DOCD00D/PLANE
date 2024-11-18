////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Language

import scala.annotation.tailrec

object Continuations:
  type Continuation[InputType, ResultType] = (InputType => ResultType) => ResultType

  def compute[InputType, ResultType](value: InputType)(cont: InputType => ResultType): ResultType = cont(value)

  // A computation that passes its result to the continuation (CPS style)
  def computation1(x: Int): Continuation[Int, Unit] = { cont =>
    println(s"Computation 1: Input = $x")
    println(s"Computation 1: Cont = $cont")
    val result = x + 1
    val ir: Unit = cont(result) // Pass result to the continuation
    println(ir)
    ir
  }

  def computation2(y: Int): Continuation[Int, Unit] = { cont =>
    println(s"Computation 2: Input = $y")
    val result = y * 2
    cont(result) // Pass result to the next continuation
  }

  def computation3(z: Int): Continuation[Int, Unit] = { cont =>
    println(s"Computation 3: Input = $z")
    val result = z - 3
    cont(result) // Final result passed to the continuation
  }

  // A function to run the continuation chain
  def runContinuations[A, B](initial: A)(continuation: Continuation[A, B]): B = {
    continuation(result => println(s"Final result: $result").asInstanceOf[B])
  }

  // Compose computations manually using CPS
  val composedContinuations: Continuation[Int, Unit] = { cont =>
    computation1(10) { result1 =>
      println(s"after comp 1: $result1")
      computation2(result1) { result2 =>
        println(s"after comp 2: $result2")
        computation3(result2)(cont) // Continue to the final continuation
      }
    }
  }

  def main(args: Array[String]): Unit = {
    val outValue: String = compute(10) {
      res1 =>
        compute(res1 * 2) {
          res2 => s"The result is ${res1 + res2}"
        }
    }
    println(outValue)
    val someComputation: [InputType, ResultType] => InputType => ResultType = [InputType, ResultType] => (x: InputType) => x.asInstanceOf[ResultType]

    def multiplyCPS[ResultType](x: Int, y: Int)(cont: Int => ResultType): ResultType = cont(x * y)
    multiplyCPS(4, 2) { multResult =>
      println(s"The final result is $multResult")
    }

    @tailrec
    def factorialCPS(n: Int, cont: Int => Int): Int =
      println(s"factorialCPS with $n")
      if n == 0 then cont(1)
      else
        factorialCPS(
           n - 1,
           res => {
             println(s"invoking cont with res $res")
             cont(n * res)
           }
        )
    val result = factorialCPS(5, identity)
    println(result) // Output: 120

    val f = computation1(10)
    f((x: Int) => ())
    runContinuations(10)(composedContinuations)
  }
