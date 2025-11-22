////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.AsyncComputations
import cats.effect.{ExitCode, IO, IOApp}

import java.util.concurrent.CompletableFuture
import java.util.function.BiFunction
import scala.util.{Failure, Success, Try}
import cats.effect.*
import cats.implicits.*
import CatsIO.Helpers.Aid4Debugging.*
import CatsIO.Helpers.{blue, bold, green, red}

import scala.concurrent.duration.DurationInt

object AsyncTemplate extends IOApp:
  def callAsync(): IO[Int] =
    IO.async { (ioCallback: Either[Throwable, Int] => Unit) =>
      IO {
        Some(IO {
          ioCallback(Right(42))
        })
//        None
      }
    }

  override def run(args: List[String]): IO[ExitCode] =
    for
      _ <- IO.println("tick".green).debugInfo()
      _ <- IO.sleep(200.millis).debugInfo()
      res <- callAsync().start
      _ <- res.cancel.debugInfo()
      _ <- IO.println(res)
      _ <- IO.println("tock".green).debugInfo()
    yield ExitCode.Success
