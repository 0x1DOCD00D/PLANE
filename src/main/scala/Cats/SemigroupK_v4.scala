package Cats

object SemigroupK_v4 {
  import cats.data.{Kleisli, ValidatedNel}
  import cats.syntax.all.*

  type V[A] = ValidatedNel[String, A]
  type Decoder[A] = Kleisli[V, String, A]

  // "42"
  val asInt: Decoder[Int] = Kleisli { s =>
    Either.catchOnly[NumberFormatException](s.toInt)
      .leftMap(_ => s"'$s' is not a decimal int").toValidatedNel
  }

  // "user-42"
  val userId: Decoder[Int] = Kleisli { s =>
    if s.startsWith("user-") then
      Either.catchOnly[NumberFormatException](s.stripPrefix("user-").toInt)
        .leftMap(_ => s"'$s' has non-integer suffix").toValidatedNel
    else s"Missing 'user-' prefix in '$s'".invalidNel
  }

  // Try decimal first, then prefixed. If both fail, errors accumulate.
  val idDecoder: Decoder[Int] = asInt <+> userId

  def main(args: Array[String]): Unit = {
    val ok1 = idDecoder.run("37") // Valid(37)
    val ok2 = idDecoder.run("user-93") // Valid(93)
    val bad = idDecoder.run("abc") // Invalid(NEL("'abc' is not a decimal int", "Missing 'user-' prefix in 'abc'"))

    println(ok1)
    println(ok2)
    println(bad)
  }
}
