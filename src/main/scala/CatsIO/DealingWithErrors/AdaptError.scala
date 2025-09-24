
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.DealingWithErrors

import cats.effect.{ExitCode, IO, IOApp}
import scala.util.{Failure, Success, Try}

object AdaptError extends IOApp:
  def log[T](message: String, instance: T): T =
    println(message + ": " + instance.toString)
    instance
  end log

  def AdaptError_Program: IO[Unit] = for {
    fib <- IO(println("/Users/drmark/IdeaProjects/PLANE/src/main/scala/CatsIO/DealingWithErrors/AdaptError.scala")).start
    l1 = log("time: 9/24/25:", fib.toString)
    _ <- fib.join
  } yield ()

  final case class CsvError(msg: String, cause: Option[Throwable] = None) extends Exception(msg, cause.orNull)

  def parseCsv(path: String): IO[List[Map[String, String]]] =
    IO.blocking {
      val nioPath = java.nio.file.Paths.get(path)
      val src = scala.io.Source.fromFile(nioPath.toFile) // throws if missing
      try src.getLines().toList.map(r => Map("row" -> r))
      finally src.close()
    }.adaptError { case t =>
      CsvError(s"CSV parse failed at $path", Some(t))
    }


  override def run(args: List[String]): IO[ExitCode] = parseCsv("/root/bad/path/file.txt").as(ExitCode.Success)
