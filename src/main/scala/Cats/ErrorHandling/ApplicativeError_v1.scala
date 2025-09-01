package Cats.ErrorHandling

import cats.ApplicativeError
import cats.*
import cats.syntax.all.*

object ApplicativeError_v1 {

  import cats.*
  import cats.syntax.all.*

  // ----- domain errors
  sealed trait ConfigError extends Product with Serializable

  object ConfigError {
    final case class MissingKey(key: String) extends ConfigError

    final case class ParseError(key: String, cause: String) extends ConfigError

    final case class InvalidBound(key: String, msg: String) extends ConfigError
  }

  // Do all the sequencing in Either, then lift once.
  // Only needs an ApplicativeError instance, no flatMap on F required.
  def readPositiveInt[F[_]](
                             cfg: Map[String, String],
                             key: String
                           )(using ApplicativeError[F, ConfigError]): F[Int] = {
    val e: Either[ConfigError, Int] =
      for {
        raw <- cfg.get(key).toRight[ConfigError](ConfigError.MissingKey(key))
        n <- Either
          .catchOnly[NumberFormatException](raw.trim.toInt)
          .leftMap(e => ConfigError.ParseError(key, e.getMessage): ConfigError)
        _ <- Either.cond(n > 0, (), ConfigError.InvalidBound(key, "must be > 0"))
      } yield n

    e.liftTo[F]
  }

  // Compose multiple reads applicatively
  def loadAll[F[_]](
                     cfg: Map[String, String]
                   )(using ApplicativeError[F, ConfigError]): F[(Int, Int)] =
    (
      readPositiveInt[F](cfg, "retries"),
      readPositiveInt[F](cfg, "timeoutMs")
    ).tupled

  // ----- example instantiation with Either
  type CfgF[A] = Either[ConfigError, A]

  val cfg = Map("retries" -> "3", "timeoutMs" -> "-1")

  val retries: CfgF[Int] = readPositiveInt[CfgF](cfg, "retries") // Right(3)
  val timeout: CfgF[Int] = readPositiveInt[CfgF](cfg, "timeoutMs") // Left(InvalidBound(...))
  val both: CfgF[(Int, Int)] = loadAll[CfgF](cfg)

  def main(args: Array[String]): Unit = {
    println(s"Retries: $retries")
    println(s"Timeout: $timeout")
    println(s"All: ${loadAll[CfgF](cfg)}")
  }
}
