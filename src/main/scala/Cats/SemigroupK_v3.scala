package Cats

import cats.data.OptionT
import cats.syntax.all.*
import cats.effect.{IO, IOApp}
import scala.concurrent.duration.*

object SemigroupK_v3 extends IOApp.Simple {

  final case class User(id: String)

  def source(name: String, hit: Boolean, delayMs: Long): OptionT[IO, User] =
    OptionT {
      IO.sleep(delayMs.millis) *>
        IO.println(s"[$name] queried, hit=$hit") *>
        IO.pure(if (hit) Some(User(s"$name-user")) else None)
    }

  // Try primary, then cache, then DB. Only as many calls as needed.
  def fetchUser(id: String): OptionT[IO, User] =
    source("primary", hit = false, delayMs = 300) <+>
      source("cache", hit = true, delayMs = 30) <+>
      source("db", hit = true, delayMs = 80)

  def run: IO[Unit] =
    fetchUser("42").value.flatMap {
      case Some(u) => IO.println(s"Fetched: $u")
      case None => IO.println("Not found anywhere")
    }
}
