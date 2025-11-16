
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scala3Features.Conversions
import scala.language.implicitConversions

object MeasurementSystem:
  case class Meters(value: Double):
    def +(other: Meters): Meters = Meters(value + other.value)
    def *(scalar: Double): Meters = Meters(value * scalar)
    override def toString: String = f"$value%.2fm"

  case class Feet(value: Double)
  case class Inches(value: Double)
  case class Kilometers(value: Double)

  given Conversion[Feet, Meters] = feet => Meters(feet.value * 0.3048)
  given Conversion[Inches, Meters] = inches => Meters(inches.value * 0.0254)
  given Conversion[Kilometers, Meters] = km => Meters(km.value * 1000)
  given Conversion[Meters, Meters] = identity

  extension [T](x: T)(using conv: Conversion[T, Meters])
    def distanceTo[U](y: U)(using convY: Conversion[U, Meters]): Meters =
      val xMeters = conv(x)
      val yMeters = convY(y)
      val diff = math.abs(xMeters.value - yMeters.value)
      Meters(diff)

    def isLongerThan[U](y: U)(using convY: Conversion[U, Meters]): Boolean =
      val xMeters = conv(x)
      val yMeters = convY(y)
      xMeters.value > yMeters.value

    def scale(factor: Double): Meters =
      val xMeters = conv(x)
      xMeters * factor

// Example 2: Rich Query Builder with Type Conversions
// ==========================================================

object QueryBuilder:

  // Target type representing a query value
  enum QueryValue:
    case StringVal(s: String)
    case NumberVal(n: Double)
    case BoolVal(b: Boolean)
    case ListVal(items: List[QueryValue])

    override def toString: String = this match
      case StringVal(s) => s"'$s'"
      case NumberVal(n) => n.toString
      case BoolVal(b) => b.toString
      case ListVal(items) => items.mkString("[", ", ", "]")

  import QueryValue.*

  case class Query(conditions: List[String]):
    def and(other: Query): Query = Query(conditions ++ other.conditions)
    override def toString: String = conditions.mkString(" AND ")

  // Conversions to QueryValue
  given Conversion[String, QueryValue] = StringVal(_)
  given Conversion[Int, QueryValue] = n => NumberVal(n.toDouble)
  given Conversion[Double, QueryValue] = NumberVal(_)
  given Conversion[Boolean, QueryValue] = BoolVal(_)
  given Conversion[List[Int], QueryValue] = nums => ListVal(nums.map(n => NumberVal(n.toDouble)))

  // Extension methods with explicit conversion
  extension [T](field: String)
    def ===[U](value: U)(using conv: Conversion[U, QueryValue]): Query =
      val queryVal = conv(value)
      Query(List(s"$field = $queryVal"))

    def in[U](values: U)(using conv: Conversion[U, QueryValue]): Query =
      val queryVal = conv(values)
      Query(List(s"$field IN $queryVal"))

    def contains[U](value: U)(using conv: Conversion[U, QueryValue]): Query =
      val queryVal = conv(value)
      Query(List(s"$field CONTAINS $queryVal"))

object ConfigSystem:
  enum ConfigValue:
    case Str(value: String)
    case Num(value: Double)
    case Bool(value: Boolean)
    case Duration(milliseconds: Long)

    def asString: String = this match
      case Str(v) => v
      case Num(v) => v.toString
      case Bool(v) => v.toString
      case Duration(ms) => s"${ms}ms"

  import ConfigValue.*

  case class Seconds(value: Int)
  case class Minutes(value: Int)
  case class Hours(value: Int)

  given Conversion[String, ConfigValue] = Str(_)
  given Conversion[Int, ConfigValue] = n => Num(n.toDouble)
  given Conversion[Boolean, ConfigValue] = Bool(_)
  given Conversion[Seconds, ConfigValue] = s => Duration(s.value * 1000L)
  given Conversion[Minutes, ConfigValue] = m => Duration(m.value * 60 * 1000L)
  given Conversion[Hours, ConfigValue] = h => Duration(h.value * 60 * 60 * 1000L)

  case class Config(settings: Map[String, ConfigValue]):
    def get(key: String): Option[ConfigValue] = settings.get(key)
    def withSetting(key: String, value: ConfigValue): Config =
      Config(settings + (key -> value))

  object Config:
    def empty: Config = Config(Map.empty)

  // Extension methods for setting configuration
  extension [T](config: Config)
    def set[U](key: String, value: U)(using conv: Conversion[U, ConfigValue]): Config =
      val configValue = conv(value)
      config.withSetting(key, configValue)

    def setIfValid[U](key: String, value: U)(using conv: Conversion[U, ConfigValue]): Either[String, Config] =
      val configValue = conv(value)
      configValue match
        case Duration(ms) if ms < 0 => Left(s"Invalid duration: $ms ms")
        case Num(n) if n.isNaN || n.isInfinite => Left(s"Invalid number: $n")
        case _ => Right(config.withSetting(key, configValue))

object TextComparison:

  // Target normalized text type
  case class NormalizedText(value: String):
    def similarity(other: NormalizedText): Double =
      // Simple Jaccard similarity on words
      val words1 = value.split("\\s+").toSet
      val words2 = other.value.split("\\s+").toSet
      val intersection = words1.intersect(words2).size.toDouble
      val union = words1.union(words2).size.toDouble
      if union == 0 then 1.0 else intersection / union

    override def toString: String = s"Normalized($value)"

  case class RawText(value: String)
  case class HTMLText(value: String)
  case class MarkdownText(value: String)

  // Conversions that perform normalization
  given Conversion[String, NormalizedText] = s =>
    NormalizedText(s.toLowerCase.trim.replaceAll("\\s+", " "))

  given Conversion[RawText, NormalizedText] = raw =>
    NormalizedText(raw.value.toLowerCase.trim.replaceAll("\\s+", " "))

  given Conversion[HTMLText, NormalizedText] = html =>
    val withoutTags = html.value.replaceAll("<[^>]+>", " ")
    NormalizedText(withoutTags.toLowerCase.trim.replaceAll("\\s+", " "))

  given Conversion[MarkdownText, NormalizedText] = md =>
    val withoutMarkdown = md.value
      .replaceAll("\\*\\*([^*]+)\\*\\*", "$1") // bold
      .replaceAll("\\*([^*]+)\\*", "$1")       // italic
      .replaceAll("\\[([^]]+)\\]\\([^)]+\\)", "$1") // links
    NormalizedText(withoutMarkdown.toLowerCase.trim.replaceAll("\\s+", " "))

  // Extension methods for text comparison
  extension [T](x: T)(using conv: Conversion[T, NormalizedText])
    def isSimilarTo[U](y: U, threshold: Double = 0.5)(using convY: Conversion[U, NormalizedText]): Boolean =
      val xNorm = conv(x)
      val yNorm = convY(y)
      xNorm.similarity(yNorm) >= threshold

    def findMostSimilar[U](candidates: List[U])(using convY: Conversion[U, NormalizedText]): Option[(U, Double)] =
      val xNorm = conv(x)
      if candidates.isEmpty then None
      else
        val scored = candidates.map { candidate =>
          val yNorm = convY(candidate)
          (candidate, xNorm.similarity(yNorm))
        }
        Some(scored.maxBy(_._2))

    def matches[U](pattern: U)(using convY: Conversion[U, NormalizedText]): Boolean =
      val xNorm = conv(x)
      val patternNorm = convY(pattern)
      xNorm.value.contains(patternNorm.value)

// Main demonstration
  @main def runMeasurementSystem(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/scala3Features/Conversions/MeasurementSystem.scala created at time 7:26PM")

    println("=" * 60)
    println("Example 1: Measurement System")
    println("=" * 60)

    import MeasurementSystem.*

    val height1 = Feet(6.0)
    val height2 = Inches(72.0)
    val height3 = Meters(2.0)

    println(s"Distance between ${height1} and ${height2}: ${height1.distanceTo(height2)}")
    println(s"Distance between ${height1} and ${height3}: ${height1.distanceTo(height3)}")
    println(s"Is ${Kilometers(0.002)} longer than ${height1}? ${Kilometers(0.002).isLongerThan(height1)}")
    println(s"Scaling ${Feet(10)} by 2.5: ${Feet(10).scale(2.5)}")
    println()

    println("=" * 60)
    println("Example 2: Query Builder")
    println("=" * 60)

    import QueryBuilder.*

    val query1: Query = "name" === "Alice"
    val query2: Query = "age" === 30
    val query3: Query = "active" === true
    val query4: Query = "scores" in List(85, 90, 95)

    val combinedQuery: Query = query1 and query2 and query3
    println(s"Combined query: $combinedQuery")
    println(s"List query: $query4")
    println()

    println("=" * 60)
    println("Example 3: Configuration System")
    println("=" * 60)

    import ConfigSystem.*

    val config = Config.empty
      .set("server.host", "localhost")
      .set("server.port", 8080)
      .set("server.ssl", true)
      .set("timeout", Seconds(30))
      .set("max-idle", Minutes(5))
      .set("session-duration", Hours(2))

    println("Configuration settings:")
    config.settings.foreach { case (key, value) =>
      println(s"  $key = ${value.asString}")
    }

    println("\nValidation examples:")
    val validConfig = config.setIfValid("retry-delay", Seconds(5))
    println(s"Valid setting: $validConfig")

    val invalidConfig = config.setIfValid("invalid-timeout", Seconds(-10))
    println(s"Invalid setting: $invalidConfig")
    println()

    println("=" * 60)
    println("Example 4: Text Comparison")
    println("=" * 60)

    import TextComparison.*

    val text1 = "Hello World from Scala"
    val text2 = RawText("  HELLO   world  FROM   scala  ")
    val text3 = HTMLText("<p>Hello <b>World</b> from <i>Scala</i></p>")
    val text4 = MarkdownText("**Hello** *World* from [Scala](https://scala-lang.org)")

    println(s"Are text1 and text2 similar? ${text1.isSimilarTo(text2)}")
    println(s"Are text1 and text3 similar? ${text1.isSimilarTo(text3)}")
    println(s"Are text1 and text4 similar? ${text1.isSimilarTo(text4)}")

    val candidates = List(
      "Hello Scala World",
      "Goodbye Java",
      "Hello from Python",
      "Scala is awesome"
    )

    val mostSimilar = text1.findMostSimilar(candidates)
    mostSimilar.foreach { case (candidate, score) =>
      println(f"\nMost similar to '$text1': '$candidate' (score: $score%.3f)")
    }

    println(s"\nDoes text3 match 'hello'? ${text3.matches("hello")}")
    println(s"Does text4 match 'Scala'? ${text4.matches("Scala")}")