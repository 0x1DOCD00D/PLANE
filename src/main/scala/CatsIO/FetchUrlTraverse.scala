
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO

import cats.effect.{IO, IOApp}
import cats.syntax.all.*

//https://typelevel.org/cats/typeclasses/traverse.html

//def traverse[F[_]: Traverse, G[_]: Applicative, A, B](fa: F[A])(f: A => G[B]): G[F[B]]

object FetchUrlTraverse extends IOApp.Simple:

  def fetchUrl(u: String): IO[Int] =
    IO.println(s"Fetching $u") *> IO.pure(u.length)

  case class Container[T](value: T)
  
  def processListInc(u: Option[Int]): IO[Option[Container[String]]] =
    IO.println(s"Incrementing $u") *> IO.pure(u.map(v=>Container((v+1).toString)))

  val urls = List("https://a15.com", "https://bbb17.com", "https://cccc18.com")

  val program: IO[List[Int]] =
    val r1: IO[List[Int]] = urls.traverse(fetchUrl)   // sequential
    val r2: IO[List[Int]] = urls.parTraverse(fetchUrl) // parallel if cats-effect 3

    //urls.map(fetchUrl).sequence // equivalent to traverse
    urls.foldLeft(IO.pure(List.empty[Int])) { (acc, url) =>
      fetchUrl(url).flatMap(content =>
        acc.map(rest => content :: rest)
      )
    }

  val programWithOptionList: IO[List[Option[Container[String]]]] =
    val data: List[Option[Int]] = List(Some(1), Some(2), None)
    data.traverse(processListInc)

  def run: IO[Unit] =
    program.flatMap(responses => IO.println(responses.mkString("\n")))
        >> programWithOptionList.flatMap(responses => IO.println(responses.mkString("\n")))