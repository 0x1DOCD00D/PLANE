
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.Concepts

import CatsIO.Helpers.Aid4Debugging.printStackContentEagerly
import cats.Eval
import cats.effect.{ExitCode, IO, IOApp}

object FactorialEval extends IOApp:
  def factorialEval(x: BigInt): Eval[BigInt] = if(x == 0) {
    Eval.now(1)
  } else {
    printStackContentEagerly()
    Eval.defer(factorialEval(x-1).map(_ * x))
  }

  def factorial(x:BigInt):BigInt = factorialEval(x).value
  override def run(args: List[String]): IO[ExitCode] =
    IO.println(factorial(100000)).as(ExitCode.Success)
