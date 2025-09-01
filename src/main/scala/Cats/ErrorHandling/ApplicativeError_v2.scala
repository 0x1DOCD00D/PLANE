package Cats.ErrorHandling

object ApplicativeError_v2 {

  import cats.*
  import cats.syntax.all.*

  final case class User(id: String, name: String)

  sealed trait RepoError extends Product with Serializable

  object RepoError {
    final case class NotFound(id: String) extends RepoError

    final case class Transport(msg: String) extends RepoError
  }

  // Pure fallback: use `recover` (no flatMap/monad required)
  def withCacheRecover[F[_]](
                              primary: => F[User], // e.g., remote fetch
                              cache: => Option[User] // pure cache (in-memory Map, etc.)
                            )(using ApplicativeError[F, RepoError]): F[User] =
    primary.recover {
      case RepoError.NotFound(id) =>
        cache.getOrElse(User(id, "Anonymous")) // choose any pure default
    }

  type RepoF[A] = Either[RepoError, A]

  val remoteMiss: RepoF[User] = Left(RepoError.NotFound("u-1"))
  val cached: Option[User] = Some(User("u-1", "Cached Carol"))
  val ok: RepoF[User] = withCacheRecover[RepoF](remoteMiss, cached)
  // Right(User("u-1","Cached Carol"))

  val netFail: RepoF[User] = Left(RepoError.Transport("TLS"))
  val stillFails: RepoF[User] = withCacheRecover[RepoF](netFail, cached)

  // Left(Transport("TLS")) because the recover only handles NotFound
  def main(args: Array[String]): Unit = {
    println(netFail)
    println(ok)
    println(stillFails)
  }
}
