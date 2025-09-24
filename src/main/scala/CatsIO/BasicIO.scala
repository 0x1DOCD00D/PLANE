
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO

import cats.effect.*
import cats.effect.unsafe.implicits.global
import Aid4Debugging.*
import cats.syntax.all.*

object BasicIO extends IOApp:
  override def run(args: List[String]): IO[ExitCode] =
    val ioeff= IO { println("print me!") }
    val raiseFromHell: IO[Int] = IO.raiseError(new RuntimeException("oh noes!"))
    val `ohNo!!`: IO[Int] = IO.delay(throw new RuntimeException("oh noes!"))

    val program =
      for {
        fib1 <- ioeff.start.showThreadAndData
        fib2 <- ioeff.start.showThreadAndData
        _ <- `ohNo!!`.attempt.showThreadAndData
        _ <- raiseFromHell.attempt.showThreadAndData
        _ <- fib1.join
        _ <- fib2.join
      } yield ()

    program.as(ExitCode.Success)



