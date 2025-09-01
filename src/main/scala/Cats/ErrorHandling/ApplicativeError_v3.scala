package Cats.ErrorHandling

object ApplicativeError_v3 {

  import cats.*
  import cats.data.{ValidatedNel, NonEmptyList}
  import cats.syntax.all.*

  // ----- domain
  sealed trait RegError

  object RegError {
    case object MissingEmail extends RegError

    case object BadAge extends RegError
  }

  final case class Registration(email: String, age: Int)

  // ----- accumulate first with Validated, then lift once
  def validate(data: Map[String, String]): ValidatedNel[RegError, Registration] =
    val emailV: ValidatedNel[RegError, String] =
      data.get("email").toValidNel(RegError.MissingEmail)

    val ageV: ValidatedNel[RegError, Int] =
      data.get("age").toValidNel(RegError.BadAge).andThen { s =>
        Either
          .catchOnly[NumberFormatException](s.toInt)
          .leftMap(_ => RegError.BadAge)
          .toValidatedNel
      }

    (emailV, ageV).mapN(Registration.apply)

  // Only needs ApplicativeError to lift the Either once.
  def register[F[_]](
                      data: Map[String, String]
                    )(using ApplicativeError[F, NonEmptyList[RegError]]): F[Registration] =
    validate(data).toEither.liftTo[F]

  // ----- demo with Either
  type RegF[A] = Either[NonEmptyList[RegError], A]
  val good: RegF[Registration] = register[RegF](Map("email" -> "a@b", "age" -> "33"))
  val bad: RegF[Registration] = register[RegF](Map.empty)

  // good = Right(Registration("a@b",33))
  // bad  = Left(NonEmptyList(MissingEmail, List(BadAge)))

  def main(args: Array[String]): Unit = {
      println(s"Good: $good")
      println(s"Bad: $bad")
  }
}
