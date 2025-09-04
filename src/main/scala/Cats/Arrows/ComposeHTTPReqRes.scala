package Cats.Arrows

import cats.effect.{ExitCode, IOApp}

object ComposeHTTPReqRes extends IOApp {
  import cats.data.{Kleisli, OptionT}
  import cats.effect.IO
  import cats.syntax.all.*

  final case class Request(path: String, headers: Map[String, String])

  final case class Response(status: Int, body: String)

  type F[A] = OptionT[IO, A]
  type Routes = Kleisli[F, Request, Response]
  type Middleware = Kleisli[F, Request, Either[Response, Request]]

  val routes: Routes =
    Kleisli { req =>
      OptionT.fromOption[IO](
        if req.path == "/hello" then Some(Response(200, s"Hello at ${req.path}"))
        else None
      )
    }

  // Left(Response) => stop, Right(Request) => continue
  val auth: Middleware =
    Kleisli { req =>
      val ok = req.headers.get("Auth").contains("ok")
      OptionT.pure[IO](if ok then Right(req) else Left(Response(401, "unauthorized")))
    }

  val reject: Kleisli[F, Response, Response] = Kleisli.ask

  def logReject: Kleisli[F, Response, Response] =
    Kleisli(resp => OptionT.liftF(IO(println(s"[REJECT] $resp"))).as(resp))

  def logThrough: Kleisli[F, Request, Request] =
    Kleisli(req => OptionT.liftF(IO(println(s"[PASS]   ${req.path}"))).as(req))

  // Compose: auth, then log both branches, then either reject or run routes.
  val authedRoutes: Routes =
    auth andThen (logReject +++ logThrough) andThen (reject ||| routes)

  // Example use
  def run(req: Request): IO[Option[Response]] =
    authedRoutes.run(req).value

  override def run(args: List[String]): IO[ExitCode] =
    val req1 = Request("/hello", Map("Auth" -> "ok"))
    val req2 = Request("/hello", Map("Auth" -> "bad"))
    val req3 = Request("/other", Map("Auth" -> "ok"))
    for
      r1 <- run(req1)
      _ <- IO(println(s"req1 => $r1"))
      r2 <- run(req2)
      _ <- IO(println(s"req2 => $r2"))
      r3 <- run(req3)
      _ <- IO(println(s"req3 => $r3"))
    yield ExitCode.Success
}
