package Cats.Arrows

import cats.effect.IOApp

object KleislyArrowMedley extends IOApp.Simple {
  import cats.data.Kleisli
  import cats.arrow.Arrow
  import cats.syntax.all.*
  import cats.effect.{IO, IOApp}

  type K[A, B] = Kleisli[IO, A, B]

  final case class Request(path: String)
  final case class Response(code: Int, body: String)

  val toStr: K[Int, String] = Kleisli(i => IO.pure(i.toString))
  val len: K[String, Int] = Kleisli(s => IO.pure(s.length))
  val pipe: K[Int, Int] = toStr.andThen(len)

  val echo: K[Request, Response] =
    Kleisli(r => IO.pure(Response(200, s"ok ${r.path}")))

  val traced: K[(Request, String), (Response, String)] =
    Arrow[[A, B] =>> Kleisli[IO, A, B]].first(echo)

  def run: IO[Unit] =
    for {
      n <- pipe.run(12345)
      _ <- IO.println(s"len => $n")
      rs <- traced.run((Request("/hello"), "T-001"))
      _ <- IO.println(s"traced => $rs")
    } yield ()
}
