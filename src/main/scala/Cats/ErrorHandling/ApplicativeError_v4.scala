package Cats.ErrorHandling

object ApplicativeError_v4 {

  import cats.*
  import cats.syntax.all.*

  /** ==============================
   * Short-circuit batch parsing with indexed errors
   * ============================== */
  sealed trait BatchError

  object BatchError {
    final case class BadRecord(index: Int, input: String, cause: String) extends BatchError
  }

  def parseOne[F[_]](s: String, i: Int)(using ApplicativeError[F, BatchError]): F[Int] =
    Either
      .catchOnly[NumberFormatException](s.toInt)
      .leftMap(_ => BatchError.BadRecord(i, s, "not an Int"))
      .liftTo[F]

  def parseAll[F[_]](inputs: List[String])(using ApplicativeError[F, BatchError]): F[List[Int]] =
    inputs.zipWithIndex.traverse { case (s, i) => parseOne[F](s, i) }

  // --- demo with Either
  type BatchF[A] = Either[BatchError, A]
  val inputs = List("10", "  20", "oops", "40")
  val result6: BatchF[List[Int]] = parseAll[BatchF](inputs)
  // Left(BadRecord(2,"oops","not an Int"))
    def main(args: Array[String]): Unit = {
        println(s"Result6: $result6")
    }
}
