
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.Concurrency

import cats.effect.{ExitCode, IO, IOApp}

import scala.util.{Failure, Success, Try}
import cats.effect.*
import cats.effect.kernel.Outcome
import cats.syntax.all.*

import scala.concurrent.duration.*
import CatsIO.Helpers.Aid4Debugging.debugInfo

object CacheRehydrateRacePair extends IOApp:
  def log[T](message: String, instance: T): T =
    println(message + ": " + instance.toString)
    instance
  end log

  def CacheRehydrateRacePair_Program: IO[Unit] = for {
    fib <- IO(println("/Users/drmark/IdeaProjects/PLANE/src/main/scala/CatsIO/Concurrency/CacheRehydrateRacePair.scala")).start
    l1 = log("time: 9/27/25:", fib.toString)
    cache <- Ref.of[IO, Map[String, Rec]](Map.empty)
    _ <- IO.println("=== scenario: cache hit (stale) ===")
    _ <- scenario(cache, hitCache = true)
    _ <- IO.println("\n=== scenario: cache miss ===")
    _ <- scenario(cache, hitCache = false)
    _ <- fib.join
  } yield ()

  final case class Rec(value: String, version: Int)

  def scenario(cache: Ref[IO, Map[String, Rec]], hitCache: Boolean): IO[Unit] =
    val key = "user:42"
    val seed =
      if hitCache then cache.update(_ + (key -> Rec("stale-value", 0)))
      else IO.unit
    for
      _ <- seed
      rec <- getWithRehydration(cache, key, hitCache)
      _ <- IO.println(s"[client] got value=${rec.value} v${rec.version}")
      cur <- cache.get
      _ <- IO.println(s"[cache-after] $cur")
    yield ()

  // ------- simulated cache + DB --------

  def cacheGet(cache: Ref[IO, Map[String, Rec]], key: String, hit: Boolean): IO[Option[Rec]] =
    IO.sleep(60.millis) *>
      (if hit then cache.get.map(_.get(key)).debug("cache")
      else IO.pure(None).debugInfo())

  def cachePut(cache: Ref[IO, Map[String, Rec]], key: String, rec: Rec): IO[Unit] =
    cache.update(_ + (key -> rec)) *> IO.println(s"[cache] refreshed -> $rec")

  def dbFetch(key: String): IO[Rec] =
    IO.sleep(200.millis) *>
      IO.pure(Rec(s"db-$key", version = (System.nanoTime % 100).toInt)).debug("db")

  // unwrap a joined fiber into a proper value (or error)
  def await[A](fib: Fiber[IO, Throwable, A]): IO[A] =
    fib.join.flatMap {
      case Outcome.Succeeded(fa) => fa
      case Outcome.Errored(e) => IO.raiseError(e)
      case Outcome.Canceled() => IO.raiseError(new RuntimeException("fiber canceled"))
    }

  /** Race cache read vs DB. If cache wins with a value, return it immediately,
   * but keep DB running to refresh cache in the background.
   * If cache misses or errors/cancels, await DB and populate cache.
   * If DB wins first, cancel the cache read and use DB.
   */
  def getWithRehydration(cache: Ref[IO, Map[String, Rec]], key: String, hitCache: Boolean): IO[Rec] =
    val cacheIO = cacheGet(cache, key, hitCache) // IO[Option[Rec]]
    val dbIO = dbFetch(key) // IO[Rec]

    IO.racePair(cacheIO, dbIO).flatMap {
      // Left = cache finished first, we get its Outcome AND the running DB fiber
      case Left((ocCache, dbFib)) =>
        ocCache match
          case Outcome.Succeeded(fOpt) =>
            fOpt.flatMap {
              case Some(rec) =>
                // Return fast path immediately, keep DB fiber to refresh in background
                await(dbFib).flatMap(dbRec => cachePut(cache, key, dbRec)).start.void *> IO.pure(rec)

              case None =>
                // Cache miss, rely on DB; keep its fiber and await it
                await(dbFib).flatTap(dbRec => cachePut(cache, key, dbRec))
            }

          case Outcome.Errored(_) | Outcome.Canceled() =>
            // Cache failed or was canceled, rely on DB
            await(dbFib).flatTap(dbRec => cachePut(cache, key, dbRec))

      // Right = DB finished first, we get the cache fiber AND DB's Outcome
      case Right((cacheFib, ocDb)) =>
        val dbValue: IO[Rec] = ocDb match
          case Outcome.Succeeded(frec) => frec
          case Outcome.Errored(e) => cacheFib.cancel *> IO.raiseError(e)
          case Outcome.Canceled() => cacheFib.cancel *> IO.raiseError(new RuntimeException("db canceled"))

        dbValue.flatTap(dbRec => cachePut(cache, key, dbRec)) <* cacheFib.cancel
    }


  override def run(args: List[String]): IO[ExitCode] = CacheRehydrateRacePair_Program.as(ExitCode.Success)
