package CatsIO
import cats.effect._
import cats.syntax.all._
import org.http4s._, org.http4s.dsl.io._, org.http4s.implicits._
object Http4sServer {
  val service = HttpRoutes.of[IO] {
    case _ =>
      IO(Response(Status.Ok))
  }

  @main def runhttp4s(): Unit =
    println("hi")
}
