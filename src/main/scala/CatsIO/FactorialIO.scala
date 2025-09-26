
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO

import cats.effect.{ExitCode, IO, IOApp}
import CatsIO.Helpers.Aid4Debugging.*

object FactorialIO extends IOApp:

  def fact(n: Int): Int =
    printStackContentEagerly()
    if n <= 0 then 1 else n * fact(n - 1)

  def factIO(n: Int): IO[BigInt] = if n <= 0 then IO.delay(1) else
    for {
      N <- IO.delay(n)
      `N-1` <- factIO(n - 1)
      _ <- printStackContent
    } yield `N-1` * N

  override def run(args: List[String]): IO[ExitCode] =
   // fact(100000)
    val program = for {
      fib <- factIO(100000).start.showThreadAndData
      result <- fib.join
      output <- putStrLn(result)
      _ = log("fact", result)
    } yield output
    program.as(ExitCode.Success)
