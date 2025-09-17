
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.Applicatives

import cats.*
import cats.syntax.all.*

object Alternative_v1 {
  final case class Parser[+A](run: String => List[(A, String)])

  object Parser {
    given Alternative[Parser] with
      def pure[A](a: A): Parser[A] =
        Parser(s => List((a, s)))

      // must be override because Applicative already defines map
      override def map[A, B](fa: Parser[A])(f: A => B): Parser[B] =
        Parser(s => fa.run(s).map { case (a, rest) => (f(a), rest) })

      // ap can also be marked override (allowed even if abstract upstream)
      override def ap[A, B](ff: Parser[A => B])(fa: Parser[A]): Parser[B] =
        Parser { s =>
          for
            (f, s1) <- ff.run(s)
            (a, s2) <- fa.run(s1)
          yield (f(a), s2)
        }

      def empty[A]: Parser[A] =
        Parser(_ => Nil)

      def combineK[A](x: Parser[A], y: Parser[A]): Parser[A] =
        Parser(s => x.run(s) ++ y.run(s))

      def combineK_v1[A](x: Parser[A], y: Parser[A]): Parser[A] =
        Parser { s =>
          val r = x.run(s)
          if r.nonEmpty then r else y.run(s)
        }
  }

  def main(args: Array[String]): Unit = {
    import Parser.*
    val pA: Parser[Char] = Parser {
      case s if s.nonEmpty && s.head == 'a' => List(('a', s.tail))
      case _                               => Nil
    }

    val pA1: Parser[Char] = Parser {
      case s if s.nonEmpty && s.head == 'a' => List(('A', s.tail))
      case _ => Nil
    }

    val pB: Parser[Char] = Parser {
      case s if s.nonEmpty && s.head == 'b' => List(('b', s.tail))
      case _                               => Nil
    }
    val pAB: Parser[Char] = pA <+> pA1 <+> pB // combineK
    println(pAB.run("abc"))
    println(pAB.run("bca"))
    println(pAB.run("xyz"))
  }
}
