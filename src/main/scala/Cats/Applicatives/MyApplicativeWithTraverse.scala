
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.Applicatives

object MyApplicativeWithTraverse:
  trait Applicative[F[_]]:
    def pure[A](a: A): F[A]

    def map2[A, B, Z](fa: F[A], fb: F[B])(f: (A, B) => Z): F[Z]

    def map[A, B](fa: F[A])(f: A => B): F[B] = map2(fa, pure(()))((a, _) => f(a))

  object Applicative:
    def apply[F[_]](using F: Applicative[F]): Applicative[F] = F

  // Traverse says: given an Applicative for F, I can flip G[F[_]] -> F[G[_]]
  trait Traverse[G[_]]:
    def traverse[F[_] : Applicative, A, B](ga: G[A])(f: A => F[B]): F[G[B]]

    def sequence[F[_] : Applicative, A](gfa: G[F[A]]): F[G[A]] = traverse(gfa)(identity)

  // Option — stops on the first None
  given optionApplicative: Applicative[Option] with
    def pure[A](a: A): Option[A] = Some(a)

    def map2[A, B, Z](fa: Option[A], fb: Option[B])(f: (A, B) => Z): Option[Z] =
      for a <- fa; b <- fb yield f(a, b)

  // Either[E, *] — fail-fast on the first Left
  given eitherApplicative[E]: Applicative[[X] =>> Either[E, X]] with
    def pure[A](a: A): Either[E, A] = Right(a)

    def map2[A, B, Z](fa: Either[E, A], fb: Either[E, B])(f: (A, B) => Z): Either[E, Z] =
      for a <- fa; b <- fb yield f(a, b)

  // Future — waits for both futures, preserves order of the List

  import scala.concurrent.{Future, ExecutionContext}

  given futureApplicative(using ec: ExecutionContext): Applicative[Future] with
    def pure[A](a: A): Future[A] = Future.successful(a)

    def map2[A, B, Z](fa: Future[A], fb: Future[B])(f: (A, B) => Z): Future[Z] =
      fa.zip(fb).map(f.tupled)

  @main def runMyApplicativeWithTraverse(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Cats/Applicatives/MyApplicativeWithTraverse.scala created at time 7:43PM")
    val optApplicative = new Applicative[Option]:
      def pure[A](a: A): Option[A] = Some(a)

      def map2[A, B, Z](fa: Option[A], fb: Option[B])(f: (A, B) => Z): Option[Z] =
        for
          a <- fa
          b <- fb
        yield f(a, b)

    val listTraverse = new Traverse[List]:
        def traverse[F[_] : Applicative, A, B](ga: List[A])(
            f: A => F[B]
            ): F[List[B]] =
            ga.foldRight(summon[Applicative[F]].pure(List.empty[B])) { (a, accF) =>
                val F = summon[Applicative[F]]
                F.map2(f(a), accF)(_ :: _)
            }
    val optionTraverse = new    Traverse[Option]:
        def traverse[F[_] : Applicative, A, B](oa: Option[A])(
            f: A => F[B]
            ): F[Option[B]] =
            oa match
                case Some(a) => summon[Applicative[F]].map(f(a))(Some(_))
                case None    => summon[Applicative[F]].pure(None)
    // === Option example ===
    def parseInt(s: String): Option[Int] = s.toIntOption
    val data = List("1","2","x","3")

    val opt: Option[List[Int]] =
      listTraverse.traverse[Option, String, Int](data)(parseInt)

    println(opt)


