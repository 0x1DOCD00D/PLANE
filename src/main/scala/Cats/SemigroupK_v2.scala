package Cats

object SemigroupK_v2 {

  import cats.data.{Validated, ValidatedNel, NonEmptyList}
  import cats.syntax.all.*

  type V[A] = ValidatedNel[String, A]

  def usd(s: String): V[BigDecimal] =
    if s.startsWith("$") then
      Either.catchOnly[NumberFormatException](BigDecimal(s.drop(1)))
        .leftMap(_ => "USD amount is malformed").toValidatedNel
    else "Missing $ prefix".invalidNel

  def eur(s: String): V[BigDecimal] =
    // Accept "12,34 €" as 12.34
    if s.endsWith("€") then
      val normalized = s.dropRight(1).trim.replace(',', '.')
      Either.catchOnly[NumberFormatException](BigDecimal(normalized))
        .leftMap(_ => "EUR amount is malformed").toValidatedNel
    else "Missing € suffix".invalidNel

  // SemigroupK: pick the first Valid, otherwise accumulate errors
  def parsePrice(s: String): V[BigDecimal] = usd(s) <+> eur(s)

  def main(args: Array[String]): Unit = {
    val a = parsePrice("$12.34") // Valid(12.34)
    val b = parsePrice("12,34 €") // Valid(12.34)
    val c = parsePrice("twelve") // Invalid(NonEmptyList("Missing $ prefix", "Missing € suffix"))
    println(a)
    println(b)
    println(c)
  }
}
