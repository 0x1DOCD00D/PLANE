
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.ExternalFrameworks

import cats.effect.{ExitCode, IO, IOApp}
import org.typelevel.log4cats.{Logger, SelfAwareLogger}
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.slf4j.LoggerFactory as JLoggerFactory
import ch.qos.logback.classic.{Level, LoggerContext, Logger as LBLogger}
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.rolling.RollingFileAppender

import scala.concurrent.duration.DurationInt
import scala.jdk.CollectionConverters.*

object LoggingThings extends IOApp {
  private val AppLoggerName = "CatsIO.ExternalFrameworks.LoggingThings"

  // Ensure root + our named logger are TRACE and the FILE appender is started & flushing
  private def ensureFileAppender(): IO[Unit] = IO.blocking {
    val ctx  = JLoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
    val root = JLoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME).asInstanceOf[LBLogger]
    root.setLevel(Level.TRACE)

    val app  = JLoggerFactory.getLogger(AppLoggerName).asInstanceOf[LBLogger]
    app.setLevel(Level.TRACE)
    app.setAdditive(true) // inherit root appenders

    // Find FILE appender on root
    val fileAppOpt = root.iteratorForAppenders().asScala.collectFirst {
      case rfa: RollingFileAppender[?] if rfa.getName == "FILE" =>
        rfa.asInstanceOf[RollingFileAppender[ILoggingEvent]]
    }

    fileAppOpt match {
      case Some(file) =>
        // turn on immediate flush to avoid empty files in short runs
        file.setImmediateFlush(true)

        // (re)start if needed (and make sure policy is started)
        val pol = file.getRollingPolicy
        if (pol != null && !pol.isStarted) pol.start()
        if (!file.isStarted) file.start()

        // small diag
        println(s"[diag] FILE path = ${file.getFile}  started=${file.isStarted}  flush=${file.isImmediateFlush}")
        println(s"[diag] root appenders = ${root.iteratorForAppenders().asScala.map(_.getName).mkString(",")}")

      case None =>
        println("[diag] No FILE appender attached to ROOT. Check <appender-ref ref=\"FILE\"/> in logback.xml")
    }
  }

  given SelfAwareLogger[IO] = Slf4jLogger.getLoggerFromName[IO](AppLoggerName)

  def program: IO[Unit] =
    for {
      _ <- ensureFileAppender()
      _ <- Logger[IO].trace("trace -> console + file")
      _ <- Logger[IO].debug("debug -> console + file")
      _ <- Logger[IO].info ("info  -> console + file")
      _ <- Logger[IO].warn ("warn  -> console + file")
      _ <- Logger[IO].error("error -> console + file")

      // give the file appender a moment to flush on very short runs
      _ <- IO.sleep(950.millis)
    } yield ()

  override def run(args: List[String]): IO[ExitCode] =
    program.as(ExitCode.Success)
}
