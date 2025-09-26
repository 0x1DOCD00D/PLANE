
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.DealingWithErrors

import cats.effect.*
import cats.implicits.*

import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global
import CatsIO.Aid4Debugging.*
import cats.effect.implicits.parallelForGenSpawn

object ParMapNWithErrors extends IOApp:
  val ok: IO[String] = IO("hi").debugInfo()
  val ko1: IO[String] = IO.raiseError[String](new RuntimeException("oh!")).debugInfo()
  val ko2: IO[String] = IO.raiseError[String](new RuntimeException("no!")).debugInfo()
  val e1: IO[Unit] = (ok, ko1).parMapN((_, _) => ())
  val e2: IO[Unit] = (ko1, ok).parMapN((_, _) => ())
  val e3: IO[Unit] = (ko1, ko2).parMapN((_, _) => ())
  val e4: IO[Unit] = (ok, ok).parMapN((_, _) => ())

  def ParMapNWithErrors_Program: IO[Unit] =
    e1.attempt.debugInfo() *>
      IO("---").debugInfo() *>
        e2.attempt.debugInfo() *>
          IO("---").debugInfo() *>
            e3.attempt.debugInfo() *>
              IO("---").debugInfo() *>
                e4.attempt.debugInfo() *>
                  IO("---").debugInfo() *> IO.pure(())

  override def run(args: List[String]): IO[ExitCode] = ParMapNWithErrors_Program.as(ExitCode.Success)
