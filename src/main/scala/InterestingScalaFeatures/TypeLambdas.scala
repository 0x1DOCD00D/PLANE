////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package InterestingScalaFeatures

object TypeLambdas:
  trait Bifunctor[F[_, _]]:
    def bimap[A, B, C, D](fab: F[A, B])(f: A => C, g: B => D): F[C, D]

  type CurriedEither[A] = [B] =>> Either[A, B]

  given Bifunctor[Either] with
    def bimap[A, B, C, D](fab: Either[A, B])(f: A => C, g: B => D): Either[C, D] =
      fab match
        case Left(a)  => Left(f(a))
        case Right(b) => Right(g(b))

  trait Functor[F[_]]:
    def map[A, B](fa: F[A])(f: A => B): F[B]

  given Functor[Option] with
    def map[A, B](fa: Option[A])(f: A => B): Option[B] = if fa.isDefined then Option(f(fa.get)) else None

  given Functor[List] with
    def map[A, B](fa: List[A])(f: A => B): List[B] = for member <- fa yield f(member)

  given Functor[[A] =>> Either[String, A]] with
    def map[A, B](fa: Either[String, A])(f: A => B): Either[String, B] =
      fa match
        case Left(err) => Left(err)
        case Right(a)  => Right(f(a))

  // A composite functor for `F` and `G` where we need to type lambda to represent the composition.
  def composedFunctor[F[_]: Functor, G[_]: Functor]: Functor[[X] =>> F[G[X]]] = new Functor[[X] =>> F[G[X]]] {
    def map[A, B](fa: F[G[A]])(f: A => B): F[G[B]] =
      summon[Functor[F]].map(fa)(ga => summon[Functor[G]].map(ga)(f))
  }

  def leftBiasedFunctor[R]: Functor[[X] =>> Either[R, X]] = new Functor[[X] =>> Either[R, X]] {
    def map[A, B](fa: Either[R, A])(f: A => B): Either[R, B] = fa match
      case Left(r)  => Left(r)
      case Right(a) => Right(f(a))
  }

  trait Transform[F[_], G[_]]:
    def transform[A](fa: F[A]): G[A]

  given Transform[Option, List] with
    def transform[A](fa: Option[A]): List[A] = fa.toList

  def applyTransform[F[_], G[_]](using t: Transform[F, G]): Transform[F, [X] =>> Either[String, G[X]]] =
    new Transform[F, [X] =>> Either[String, G[X]]] {
      def transform[A](fa: F[A]): Either[String, G[A]] = Right(t.transform(fa))
    }

  def mapOver[F[_]: Functor, G[_]: Functor, A, B](fga: F[G[A]])(f: A => B): F[G[B]] =
    summon[Functor[F]].map(fga)(ga => summon[Functor[G]].map(ga)(f))

  @main def runTL(): Unit =
    type MapWithKey[K] = [V] =>> Map[K, V]
    val map: MapWithKey[String][Int] = Map("one" -> 1, "two" -> 2)
    println(map)

    def transformEither = summon[Bifunctor[Either]].bimap(Left(58): Either[Int, String])(_.toString, _.toUpperCase)
    println(transformEither)

    val resultA: CurriedEither[String][Int] = Right(100)
    println(resultA)

    val optionListFunctor = composedFunctor[Option, List]
    val resultB = optionListFunctor.map(Some(List(1, 2, 3)))(_ + 1)
    println(resultB)

    val resultC = summon[Functor[[A] =>> Either[String, A]]].map(Right(10))(_ * 2)
    println(resultC)

    val eitherStringFunctor = leftBiasedFunctor[String]
    val mapped = eitherStringFunctor.map(Right(10))(_ * 2)
    println(mapped)

    val optionToEitherList = applyTransform[Option, List]
    val transformed = optionToEitherList.transform(Some(10))
    println(transformed)

    val resultMap = mapOver(Option(List(1, 2, 3)))(_ + 1)
    println(resultMap)
