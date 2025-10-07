
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.ExternalFrameworks

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits.toFoldableOps

import scala.util.{Failure, Success, Try}
import doobie.*
import doobie.implicits.*
import doobie.util.transactor.Strategy

import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.{Properties, UUID}

object DoobieDB extends IOApp:
  def log[T](message: String, instance: T): T =
    println(message + ": " + instance.toString)
    instance
  end log

  def DoobieDB_Program: IO[Unit] = for {
    fib <- IO(println("/Users/drmark/IdeaProjects/PLANE/src/main/scala/CatsIO/ExternalFrameworks/DoobieDB.scala")).start
    l1 = log("time: 10/6/25:", fib.toString)
    _ <- ping
    r <- recent
    rows <- selectAll.transact(xa)
    _    <- IO.println(s"rows = ${rows.size}")
    _    <- rows.traverse_(e => IO.println(printEvent(e)))
    _ <- fib.join
  } yield ()

  private def prop(key: String, default: => String = ""): String =
    sys.props.getOrElse(key, default)

  private val driver = prop("db.driver", "com.clickhouse.jdbc.ClickHouseDriver")
  private val url = prop("db.url", "jdbc:clickhouse:http://localhost:8123/demo")
  private val user = prop("db.user", "default")
  private val pass = prop("db.pass")

  println(s"driver = $driver, url = $url, user = $user, pass = $pass")
  private val jdbcProps: Properties = {
    val p = new Properties()
    p.setProperty("user", user) // <-- NOT "db.user"
    p.setProperty("password", pass) // <-- NOT "db.pass"
    p
  }

  val xa: Transactor[IO] = Transactor.fromDriverManager[IO](
      "com.clickhouse.jdbc.ClickHouseDriver",
      "jdbc:clickhouse:http://localhost:8123/demo",
      jdbcProps,
      None
    ).copy(strategy0 = Strategy(FC.unit, FC.unit, FC.unit, FC.unit))

  val ping: IO[Unit] = sql"SELECT 1".query[Int].unique.transact(xa).flatMap(n => IO.println(s"Ping = $n"))

  final case class Event(
                          event_time: LocalDateTime,
                          user_id: Long, // UInt32 fits in Long on JVM
                          session_id: UUID,
                          event_type: String,
                          props: String // if JSON column, read as String
                        )

  private def tsToLdt(ts: Timestamp): LocalDateTime = ts.toLocalDateTime

  private def strToUuid(s: String): UUID = UUID.fromString(s)

  private val selectAll: ConnectionIO[List[Event]] =
    sql"""
        SELECT event_time, user_id, session_id, event_type, props
        FROM demo.events
        ORDER BY event_time
      """
      .query[(Timestamp, Long, String, String, String)]
      .to[List]
      .map(_.map { case (ts, uid, sid, et, pr) =>
        Event(ts.toLocalDateTime, uid, UUID.fromString(sid), et, pr)
      })

  private def printEvent(e: Event): String =
    s"[time=${e.event_time}] user=${e.user_id}, session=${e.session_id}, type=${e.event_type}, props=${e.props}"

  val recent: IO[List[(Long, String)]] =
    sql"""
         SELECT count() AS rows, max(event_type) AS any_type
         FROM demo.events
       """.query[(Long, String)].to[List].transact(xa)

  override def run(args: List[String]): IO[ExitCode] = DoobieDB_Program.as(ExitCode.Success)
