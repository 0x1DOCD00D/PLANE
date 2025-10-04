////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.DealingWithErrors

import CatsIO.Helpers
import CatsIO.Helpers.{bold, green}
import cats.MonadError
import cats.effect.{ExitCode, IO, IOApp}
import cats.instances.either.*
import cats.syntax.all.*

object CleaningTheMessFromErrors extends IOApp:
  def CheckTheGuess[F[_]](guess: Int): MonadError[F, Throwable] ?=> F[Int] =
    if(guess >= 100) guess.pure[F]
    else new Exception("Guess must be greater than or equal to 100").raiseError[F, Int]
  
  override def run(args: List[String]): IO[ExitCode] =
    val program = for {
      result <- CheckTheGuess(3)
    } yield result

    import Helpers.red
    program match
      case Left(msg) => IO.println(msg.getMessage.red.bold).as(ExitCode.Error)
      case Right(guess) => IO.println(s"Correct number ${guess*2}".green).as(ExitCode.Success)
