package Cats.Arrows

import cats.effect.{ExitCode, IOApp}

object KleisliPipeline extends IOApp {

  import cats.arrow.Arrow
  import cats.data.Kleisli
  import cats.effect.IO
  import cats.syntax.all.*

  type K[A, B] = Kleisli[IO, A, B]

  def fanout[F[_, _] : Arrow, A, B, C](f: F[A, B], g: F[A, C]): F[A, (B, C)] =
    Arrow[F].lift((a: A) => (a, a)) >>> (f *** g)

  final case class UserId(value: String)

  final case class Profile(name: String)

  final case class Balance(amount: BigDecimal)

  final case class Audit(p: Profile, b: Balance)

  final case class Pay(p: Profile, b: Balance)

  val fetchProfile: K[UserId, Profile] =
    Kleisli(id => IO.pure(Profile(s"user:${id.value}")))

  val fetchBalance: K[UserId, Balance] =
    Kleisli(_ => IO.pure(Balance(BigDecimal(125.50))))

  val enrich: K[UserId, (Profile, Balance)] =
    fanout(fetchProfile, fetchBalance)

  val decide: K[(Profile, Balance), Either[Audit, Pay]] =
    Kleisli { case (p, b) =>
      IO.pure(if b.amount < 0 then Left(Audit(p, b)) else Right(Pay(p, b)))
    }

  val auditFlow: K[Audit, Unit] = Kleisli(a => IO(println(s"AUDIT: $a")))
  val payFlow: K[Pay, Unit] = Kleisli(p => IO(println(s"PAY:   $p")))

  val pipeline: K[UserId, Either[Unit, Unit]] =
    enrich >>> decide >>> (auditFlow +++ payFlow)

  override def run(args: List[String]): IO[ExitCode] = {
    pipeline.run(UserId("42")).flatMap(v => IO.println(s"Got: $v")).as(ExitCode.Success)
  }
}
