package Cats.Arrows

import cats.effect.{ExitCode, IOApp}

/*
Kleisli[F, A, B] is a tiny wrapper around a function A => F[B].
It lets you compose effectful functions as if they were plain functions.
	•	Run: kleisli.run(a): F[B]
	•	Compose: f andThen g where f: A => F[B], g: B => F[C], keeps you in Kleisli[F, A, C]
	•	Transform inputs: local(h) to pre-map the input
	•	Lift values/effects: Kleisli.pure, Kleisli.liftF
	•	Change effects: mapK (via a FunctionK)

Because Cats gives Kleisli instances for Category, Arrow, ArrowChoice, Monad (in its result), you can stick to one abstraction and keep code uniform.
*/

object Kleisli_Basics extends IOApp {
  import cats.data.Kleisli
  import cats.effect.IO
  import cats.syntax.all.*

  type KI[A, B] = Kleisli[IO, A, B]

  // lift a pure function
  val asString: KI[Int, String] =
    Kleisli(i => IO.pure(i.toString))

  // lift an IO directly (ignores input)
  val getTime: KI[Any, Long] =
    Kleisli.liftF(IO.realTime.map(_.toMillis))

  val getTimeLogged: KI[Any, Long] =
    getTime.flatTap(t => Kleisli.liftF(IO.println(s"Current time in millis: $t")))

  // compose effectful functions A=>IO[B] and B=>IO[C]
  val length: KI[String, Int] =
    Kleisli(s => IO.pure(s.length))

  val pipeline: KI[Int, Int] =
    asString andThen length

  // adjust input shape without touching the inner logic
  val onlySecond: KI[(Int, String), Int] = pipeline.local[(Int, String)](_._1)

  override def run(args: List[String]): IO[ExitCode] =
    val r1: IO[Unit] = getTime.run(()).flatMap(t => IO.println(s"Current time in millis directly from KI: $t"))
    val gt: IO[Long] = getTime("ignored") // IO[Long]
    // run the Kleisli with some inputs
    val p1: IO[Int] = pipeline.run(12345) // IO(5)
    val p2: IO[Int] = onlySecond.run((999, "hello")) // IO(5)
    val t: IO[Long] = getTime.run(()) // IO(current time in millis)
    val res: IO[Unit] = (gt, p1, p2, t).parMapN((x, a, b, c) => s"gt = $x, p1=$a, p2=$b, time=$c").flatMap(s => IO(println(s)))
    r1 >> res.as(ExitCode.Success)
  }
