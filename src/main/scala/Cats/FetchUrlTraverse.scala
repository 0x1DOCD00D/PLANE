package Cats

import cats.effect.{IO, IOApp}
import cats.syntax.all.*

//https://typelevel.org/cats/typeclasses/traverse.html

object FetchUrlTraverse extends IOApp.Simple:

  def fetchUrl(u: String): IO[Int] =
    IO.println(s"Fetching $u") *> IO.pure(u.length)

  val urls = List("https://a15.com", "https://bbb17.com", "https://cccc18.com")

  val program: IO[List[Int]] =
    urls.traverse(fetchUrl)   // sequential
    urls.parTraverse(fetchUrl) // parallel if cats-effect 3

    //urls.map(fetchUrl).sequence // equivalent to traverse
    urls.foldLeft(IO.pure(List.empty[Int])) { (acc, url) =>
      fetchUrl(url).flatMap(content =>
        acc.map(rest => content :: rest)
      )
    }

  def run: IO[Unit] =
    program.flatMap(responses => IO.println(responses.mkString("\n")))