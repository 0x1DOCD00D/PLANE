package Cats.Arrows

import cats.effect.{ExitCode, IOApp}

object HttpWithLogging extends IOApp {

  import cats.data.Kleisli
  import cats.effect.IO

  final case class UserId(v: String)

  final case class Profile(name: String)

  final case class Html(value: String)

  trait Http:
    def get(path: String): IO[String]

  trait Logger:
    def info(msg: String): IO[Unit]

  final case class Env(http: Http, log: Logger)

  type App[A] = Kleisli[IO, Env, A]

  val askEnv: App[Env] = Kleisli.ask[IO, Env]

  // Needs only Http
  def fetchProfile(id: UserId): Kleisli[IO, Http, Profile] =
    Kleisli(http => http.get(s"/users/${id.v}/profile").map(Profile(_)))

  // Needs only Logger
  val render: Kleisli[IO, Profile, Html] =
    Kleisli(p => IO.pure(Html(s"<h1>${p.name}</h1>")))

  // Wire it together for Env with .local
  def showUser(id: UserId): App[Html] =
    fetchProfile(id).local[Env](_.http) andThen
      render

  // Add logging without changing business code
  def withLogging[A](name: String)(k: App[A]): App[A] =
    Kleisli { env =>
      env.log.info(s"start $name") *>
        k.run(env).guarantee(env.log.info(s"done  $name"))
    }

  val program: App[Html] = withLogging("showUser")(showUser(UserId("42")))

  override def run(args: List[String]): IO[ExitCode] = {
    val http = new Http {
      def get(path: String): IO[String] =
        IO.pure(s"Response from $path")
    }
    val log = new Logger {
      def info(msg: String): IO[Unit] = IO.println(s"[INFO] $msg")
    }
    val env = Env(http, log)
    program.run(env).flatMap(html => IO.println(s"Got HTML: ${html.value}")).as(ExitCode.Success)
  }
}
