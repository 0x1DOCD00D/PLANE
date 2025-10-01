
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.AsyncComputations

import CatsIO.Helpers.Aid4Debugging.debugInfo
import CatsIO.Helpers.{green, red}
import cats.effect.{ExitCode, IO, IOApp}

import scala.concurrent.duration.*

object AsyncTimer extends IOApp:

  def sleepAsync(ms: Long): IO[Unit] =
    IO.async { 
      cb =>
        IO { // <-- return IO[Option[IO[Unit]]]
          val t = new Thread(() =>
            try
              Thread.sleep(ms)
              cb(Right(()))
            catch
              case e: Throwable => cb(Left(e))
          )
          t.setDaemon(true)
          t.start()
          Some(IO {
            println(s"[cancel] sleep($ms)".red)
            t.interrupt()
          })
        }.debugInfo()
    }

  def run(args: List[String]): IO[ExitCode] =
    for
      _      <- IO.println("tick".green).debugInfo()
      fiber  <- sleepAsync(1000).start.debugInfo()
      _      <- IO.sleep(200.millis).debugInfo()
      _      <- fiber.cancel
      _      <- IO.println("tock".green).debugInfo()
    yield ExitCode.Success
