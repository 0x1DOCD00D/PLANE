
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.AsyncComputations

import cats.effect.{ExitCode, IO, IOApp}

import scala.util.{Failure, Success, Try}

object AsyncCallbackWrap extends IOApp:

  // Legacy-style API
  def legacyCompute(x: Int, cb: Either[Throwable, Int] => Unit): () => Unit =
    val t = new Thread(() =>
      try
        Thread.sleep(150)
        if x == 0 then cb(Left(new IllegalArgumentException("x=0")))
        else cb(Right(x * 2))
      catch
        case e: Throwable => cb(Left(e))
    )
    t.setDaemon(true)
    t.start()
    () => t.interrupt() // cancel handle

  def computeIO(x: Int): IO[Int] =
    IO.async { cb =>
      IO {
        val cancel = legacyCompute(x, cb)
        Some(IO {
          println(s"[cancel] legacyCompute($x)")
          cancel()
        })
      }
    }

  def run(args: List[String]): IO[ExitCode] =
    for
      a <- computeIO(21)
      _ <- IO.println(s"got: $a")
      _ <- computeIO(0).attempt.flatMap(r => IO.println(s"error path: $r"))
    yield ExitCode.Success
