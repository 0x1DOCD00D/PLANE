
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.Applicatives

object Alternative_v2 {
  //> using dep "org.typelevel::cats-core:2.10.0"

  import cats.*
  import cats.syntax.all.*

  // ----- Parser + Alternative instance -----
  final case class Parser[+A](run: String => List[(A, String)])

  object Parser {
    given Alternative[Parser] with
      def pure[A](a: A): Parser[A] = Parser(s => List((a, s)))

      def ap[A, B](ff: Parser[A => B])(fa: Parser[A]): Parser[B] =
        Parser { s =>
          for
            (f, s1) <- ff.run(s)
            (a, s2) <- fa.run(s1)
          yield (f(a), s2)
        }

      def empty[A]: Parser[A] = Parser(_ => Nil)

      def combineK[A](x: Parser[A], y: Parser[A]): Parser[A] =
        Parser(s => x.run(s) ++ y.run(s)) // nondeterministic choice
  }

  import Parser.given

  // ----- tiny primitives -----
  def satisfy(p: Char => Boolean): Parser[Char] = Parser { s =>
    s.headOption match
      case Some(ch) if p(ch) => List((ch, s.tail))
      case _ => Nil
  }

  def digit: Parser[Char] = satisfy(_.isDigit)

  // Keep only full parses
  def parseAll[A](p: Parser[A], s: String): List[A] =
    p.run(s).collect { case (a, rest) if rest.isEmpty => a }

  // ===== Using `ap` three ways =====

  // 1) Extension `.pure[Parser]` + `.ap`
  val twoDigitsViaAp: Parser[String] =
    ((c1: Char) => (c2: Char) => s"$c1$c2").pure[Parser] // Parser[Char => Char => String]
      .ap(digit) // Parser[Char => String]
      .ap(digit) // Parser[String]

  // 2) Same thing with the typeclass call
  val twoDigitsViaAp2: Parser[String] =
    Applicative[Parser].pure((c1: Char) => (c2: Char) => s"$c1$c2")
      .ap(digit)
      .ap(digit)

  // 3) Sanity check: `ap` ≡ `mapN(_(_))`
  val twoDigitsViaMapN: Parser[String] =
    (digit, digit).mapN((f, s) => s"$f$s") // same result as above

  @main def demo(): Unit =
    println(parseAll(twoDigitsViaAp, "42")) // List(42)
    println(parseAll(twoDigitsViaAp2, "42")) // List(42)
    println(parseAll(twoDigitsViaMapN, "42")) // List(42)
 }
