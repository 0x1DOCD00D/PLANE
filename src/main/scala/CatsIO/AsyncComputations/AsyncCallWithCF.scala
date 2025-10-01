
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.AsyncComputations

import CatsIO.Helpers.Aid4Debugging.debugInfo
import CatsIO.Helpers.{bold, green}
import cats.effect.{ExitCode, IO, IOApp}

import java.util.concurrent.CompletableFuture

object AsyncCallWithCF extends IOApp:
  def fromCF[A](mk: => CompletableFuture[A]): IO[A] =
    IO.async { cb =>
      IO {
        val cf = mk
        cf.whenComplete((a: A, e: Throwable) =>
          if e == null then cb(Right(a)) else cb(Left(e))
        )
        Some(IO(cf.cancel(true))) // cancellation forwards to CF
      }
    }

  def run(args: List[String]): IO[ExitCode] =
    for
      cf <- IO.delay(CompletableFuture.supplyAsync(() => {
        Thread.sleep(500)
        "callback in action"
      })).debugInfo()
      a  <- fromCF(cf).debugInfo()
      _  <- IO.println(s"result = $a".green.bold).debugInfo()
    yield ExitCode.Success
