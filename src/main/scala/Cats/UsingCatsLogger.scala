////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats

import cats.effect.IOApp
import org.typelevel.log4cats.Logger

object UsingCatsLogger extends IOApp {
  import cats.effect.{ExitCode, IO}
  import org.typelevel.log4cats.slf4j.Slf4jLogger
  import Cats.LoggerWithCats.*

  given logger: Logger[IO] = Slf4jLogger.getLogger[IO]

  private def risky(i: Int): IO[Int] =
    if i >= 0 then IO.pure(i * 2)
    else IO.raiseError(new IllegalArgumentException(s"negative input: $i"))

  override def run(args: List[String]): IO[ExitCode] = {
    val start = Logger[IO].info("Starting computation...")
    val prog =
      for
        _ <- risky(3).log(
           success = v => s"Computation succeeded with $v",
           error = e => s"Computation failed: ${e.getMessage}"
        )
        _ <- risky(-1).logError(e => s"Only log errors: ${e.getMessage}")
      yield ()

    start *> 
      prog *>
      Logger[IO].info("Finished computation...").as(ExitCode.Success)
  }
}
