package Cats.ErrorHandling

object ApplicativeError_v5 {

  import cats.*
  import cats.syntax.all.*

  /** ==============================
   * Error-aware logging via attempt
   * ============================== */
  // Works for any F with errors E; needs monadic sequencing for logging.
  def withLogging[F[_], E, A](
                               fa: F[A]
                             )(onError: E => F[Unit], onSuccess: A => F[Unit])(
                               using ME: MonadError[F, E]
                             ): F[A] =
    fa.attempt.flatMap {
      case Left(e) => onError(e) >> ME.raiseError[A](e)
      case Right(a) => onSuccess(a) >> a.pure[F]
    }

  // --- demo with Either (toy "logger")
  type LogF[A] = Either[String, A]

  def logLine(s: String): LogF[Unit] = Right(println(s)) // replace println in real effects

  val ok7: LogF[Int] =
    withLogging[LogF, String, Int](Right(7))(
      onError = e => logLine(s"ERR: $e"),
      onSuccess = a => logLine(s"OK:  $a")
    )
  // prints "OK:  7" and returns Right(7)

  val bad7: LogF[Int] =
    withLogging[LogF, String, Int](Left("boom"))(
      onError = e => logLine(s"ERR: $e"),
      onSuccess = a => logLine(s"OK:  $a")
    )
  // prints "ERR: boom" and returns Left("boom")
  def main(args: Array[String]): Unit = {
      println(s"ok7: $ok7")
      println(s"bad7: $bad7")
  }
}
