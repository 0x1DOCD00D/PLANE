////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats

import cats.*, cats.syntax.all.*

object FunctorParser {
  trait ParserX[+A]:
    def parse(s: String): Option[(A, String)]

  object ParserX:
    given Functor[ParserX] with
      def map[A, B](fa: ParserX[A])(f: A => B): ParserX[B] =
        (s: String) => fa.parse(s).map { case (a, rest) => (f(a), rest) }

    def char(c: Char): ParserX[Char] = (s: String) => s.headOption.filter(_ == c).map(ch => ch -> s.tail)

    val digit: ParserX[Int] =
      (s: String) => s.headOption.filter(_.isDigit).map(d => d.asDigit -> s.tail)

  import ParserX.*

  // Parse 2 digits, then construct a YearMonth with map over the tuple
  def pair[A](pa: ParserX[A], pb: ParserX[A]): ParserX[(A, A)] =
    (s: String) => for
      (a, s1) <- pa.parse(s)
      _ = println(s"pa: $pa, a: $a, s1: $s1")
      (b, s2) <- pb.parse(s1)
    yield (a, b) -> s2

  final case class YearMonth(year: Int, month: Int)

  val ym: ParserX[YearMonth] =
    pair(ParserX.digit, ParserX.digit).map { case (y, m) => YearMonth(2000 + y, m) }

  @main def runFunctorParser(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Cats/FunctorParser.scala created at time 2:17PM")
    println(ym.parse("12abc"))
    println(ym.parse("1abc"))

}
