package Cats.Arrows

import cats.effect.{ExitCode, IOApp}

object CorrIdThroughKleisli extends IOApp {
  import cats.arrow.Arrow
  import cats.data.Kleisli
  import cats.effect.IO
  import cats.syntax.all.*
  import java.util.UUID

  final case class Request(path: String, headers: Map[String, String])
  final case class Response(status: Int, body: String)
  final case class TraceId(value: String)

  type K[A, B] = Kleisli[IO, A, B]

  val handler: K[Request, Response] =
    Kleisli(req => IO.pure(Response(200, s"ok ${req.path}")))

  // Attach a TraceId to the input
  val attachTrace: K[Request, (Request, TraceId)] =
    Kleisli(req => IO(TraceId(UUID.randomUUID().toString)).map(tid => (req, tid)))

  // Lift `first(handler)` => (Request, TraceId) => (Response, TraceId)
  //Arrow[K].first(handler)   has type K[(Request, TraceId), (Response, TraceId)]
  val traced: K[(Request, TraceId), (Response, TraceId)] =
  // traced.run((req, tid)) == handler.run(req).map(resp => (resp, tid))
    Arrow[K].first(handler)

  // Log and drop the TraceId
  val logAndDrop: K[(Response, TraceId), Response] =
    Kleisli { case (resp, tid) =>
      IO(println(s"[trace=${tid.value}] -> ${resp.status}")).as(resp)
    }

  val handlerWithTrace: K[Request, Response] =
    attachTrace >>> traced >>> logAndDrop

  override def run(args: List[String]): IO[ExitCode] = {
    val req = Request("/hello", Map("Auth" -> "ok"))
    val tid = TraceId("T-001")

    val outIO: IO[(Response, TraceId)] = traced.run((req, tid))
    outIO.flatMap { case (resp, t) => IO.println(s"[direct] trace=${t.value} -> $resp") } *>
    //handler.run(Request("/my/path", Map("Auth" -> "ok"))).flatMap(resp => IO.println(s"Got: $resp")).as(ExitCode.Success)
    //>>
    handlerWithTrace.run(Request("/my/path", Map("Auth" -> "ok"))).flatMap(resp => IO.println(s"Got: $resp")).as(ExitCode.Success)
  }

}
