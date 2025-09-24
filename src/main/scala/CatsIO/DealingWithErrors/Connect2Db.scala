
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.DealingWithErrors

import cats.effect.IO.{IOCont, Uncancelable}
import cats.effect.{ExitCode, IO, IOApp}
import cats.syntax.all.*

import scala.util.{Failure, Success, Try}

object Connect2Db extends IOApp:
  def log[T](message: String, instance: T): T =
    println(message + ": " + instance.toString)
    instance
  end log

  sealed trait DbError extends Product with Serializable

  object DbError {
    final case class Connection(msg: String) extends DbError

    final case class Protocol(msg: String) extends DbError

    final case class Unknown(t: Throwable) extends DbError
  }

  def unsafeConnect(url: String): Boolean = {
    if url.contains("bad") then throw new java.net.ConnectException("refused")
    else if url.contains("weird") then throw new IllegalStateException("protocol")
    else true
  }

  def connectIO(url: String): IO[Either[DbError, Boolean]] =
    IO.blocking(unsafeConnect(url)).attempt.map {
      case Right(c) => Right(c)
      case Left(e: java.net.ConnectException) => Left(DbError.Connection(e.getMessage))
      case Left(e: IllegalStateException) => Left(DbError.Protocol(e.getMessage))
      case Left(other) => Left(DbError.Unknown(other))
    }

  def render(e: DbError): String = e match {
    case DbError.Connection(m) => s"connection error: $m"
    case DbError.Protocol(m) => s"protocol error: $m"
    case DbError.Unknown(t) => s"unexpected: ${t.getClass.getSimpleName}: ${t.getMessage}"
  }

  def report(url: String): IO[Unit] =
    connectIO(url).flatMap {
      case Right(_) => IO.println(s"[ok] connected to $url")
      case Left(e) => IO.println(s"[err] $url -> ${render(e)}")
    }

  def reportS(url: String): IO[String] =
    connectIO(url).flatMap {
      case Right(_) => IO(s"[ok] connected to $url")
      case Left(e) => IO(s"[err] $url -> ${render(e)}")
    }


  def Connect2Db_Program: IO[Unit] = for {
    fib <- IO(println("/Users/drmark/IdeaProjects/PLANE/src/main/scala/CatsIO/DealingWithErrors/Connect2Db.scala")).start
    l1 = log("time: 9/24/25:", fib.toString)
    _ <- fib.join
  } yield ()

  override def run(args: List[String]): IO[ExitCode] =
    val urls = List("http://good.url", "http://bad.url", "http://weird.url")
    val res: IO[List[String]] = for {
      msgs <- IO.parTraverse(urls)(reportS)
      _ <- IO.println(msgs.mkString("\n"))
    } yield msgs //ExitCode.Success

    res.flatMap(lst => IO.println(lst.mkString("\n")))
    res.as(ExitCode.Success)
    val ios: List[IO[String]] =
      for {
        url <- urls
        ioOutcome = reportS(url) // NOTE: '=' not '<-'
      } yield ioOutcome
    val msgsT: IO[List[String]] = ios.sequence
    val msgs: IO[Unit] = msgsT.flatMap(lst => IO.println(lst.mkString("\n")))
    msgs.as(ExitCode.Success)
//    IO.parTraverse(urls)(report).void.as(ExitCode.Success)
    //report("http://bad.url").as(ExitCode.Success)
