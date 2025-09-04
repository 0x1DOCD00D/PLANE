package Cats.Arrows

import cats.effect.IOApp

/*
Kleisli[F, A, B]      // A => F[B]
k.run(a)              // F[B]
Kleisli.pure(b)       // pure constant
Kleisli.liftF(fb)     // lift F[B]
Kleisli.ask[F, A]     // A inside F

k1 andThen k2         // compose
k.local(h)            // pre-map input
k.map(b => c)         // post-map result
k.flatMap(b => k2)    // dependent composition
k.mapK(nat)           // change effect F ~> G

// Arrow/Choice (need Monad[F]):
f *** g               // (A => B, C => D), on tuples
f +++ g               // map both sides of Either
f ||| g               // choose branch of Either
*/

object Kleisli_Cheat_Sheet extends IOApp {

  import cats.data.Kleisli
  import cats.effect.IO
  import cats.syntax.all.*
  import cats.arrow.FunctionK

  type K[A, B] = Kleisli[IO, A, B]

  /*
  	General constructor: you supply a function Int => IO[String].
	Can use the input if you want.
	In your specific code it ignores i, so it’s effectively constant.
	Equivalent to Kleisli.liftF(IO.pure("x")) only because your function doesn’t depend on i.
  * */
  val k: K[Int, String] = Kleisli((i: Int) => IO.pure(i.toString)) // Kleisli[F, A, B]

  val toStr: K[Int, String] = Kleisli(i => IO.pure(i.toString))
  val useFirst: Kleisli[IO, (Int, Unit), String] = toStr.local(e => e._1)

  val ioS: IO[String] = k.run(7) // k.run(a): F[B]
  /*
  	Builds a constant arrow using Applicative[IO].pure.
	No side effects are encoded here; it always yields "x" purely.
	Requires an Applicative for IO (which exists).
  * */
  val kp: K[Int, String] = Kleisli.pure[IO, Int, String]("x") // Kleisli.pure
  /*
  	Lifts an existing effect IO[String] to a Kleisli that ignores input.
	Whatever effects are inside that IO will run each time the Kleisli is executed.
	Use this when you want a constant Kleisli that can still perform effects.
  * */
  val kl: K[Int, String] = Kleisli.liftF[IO, Int, String](IO.pure("x")) // Kleisli.liftF
  val askI: K[Int, Int] = Kleisli.ask[IO, Int] // Kleisli.ask[F, A]

  // Not expressible with pure/liftF alone, since those ignore input
  val len: K[String, Int] = Kleisli((s: String) => IO.pure(s.length))
  //effectConst prints "hi" each time it is run
  val effectConst: K[Int, String] = Kleisli.liftF(IO.println("hi from effect const!") *> IO.pure("x")) // prints each run
  val comp: K[Int, Int] = k.andThen(len) // k1 andThen k2
  val kLoc: K[(Int, String), String] = k.local[(Int, String)](_._1) // k.local(h)
  val kMap: K[Int, Int] = Kleisli((i: Int) => IO.pure(i)).map(_ + 1) // k.map
  val kFlat: K[Int, Int] = Kleisli((i: Int) => IO.pure(i)).flatMap(x => Kleisli.pure[IO, Int, Int](x + 1)) // k.flatMap
  val kMapK: K[Int, String] = k.mapK(FunctionK.id[IO]) // k.mapK(nat)

  // Want a Kleisli that always fails? Use liftF with a failing IO.
  val alwaysFail: K[Int, String] = Kleisli.liftF(IO.raiseError(new RuntimeException("boom")))

  val split: K[(Int, String), (Int, Int)] =
    (Kleisli((i: Int) => IO.pure(i + 1)) *** Kleisli((s: String) => IO.pure(s.length))) // f *** g

  val bothSides: K[Either[Int, String], Either[Int, Int]] =
    Kleisli((i: Int) => IO.pure(i + 1)) +++ Kleisli((s: String) => IO.pure(s.length)) // f +++ g

  val choose: K[Either[Int, String], String] =
    Kleisli((i: Int) => IO.pure(i.toString)) ||| Kleisli((s: String) => IO.pure(s)) // f ||| g

  override def run(args: List[String]) =
    def show[A](label: String, io: IO[A]): IO[Unit] =
      io.flatMap(v => IO.println(s"$label = $v"))

    // run a bunch of actions in parallel and print results as they come
    val actions = List(ioS, kp.run(0), kl.run(0), askI.run(42), comp.run(123), kLoc.run((999, "hello")), kMap.run(5), kFlat.run(5), kMapK.run(8), split.run((5, "hello")), bothSides.run(Left(7)), bothSides.run(Right("hello")), choose.run(Left(9)), choose.run(Right("world")))
    actions.parSequence.flatMap(res => IO(res.foreach(println))).as(cats.effect.ExitCode.Success)

    /*
    for {
      _ <- show("k.run(7)", k.run(7))
      _ <- show("ioS",      ioS)
    } yield ()
        |
       \/
    show("k.run(7)", k.run(7)).flatMap(_ =>
    show("ioS", ioS).map(_ => () ))
    */
    for {
      _ <- show("k.run(7)", k.run(7))
      _ <- show("ioS", ioS)
      _ <- show("kp.run(123)", kp.run(123))
      _ <- show("kl.run(999)", kl.run(999))
      _ <- show("effectConst.run(0)", effectConst.run(0))
      _ <- show("askI.run(5)", askI.run(5))
      _ <- show("len.run(\"hello\")", len.run("hello"))
      _ <- show("comp.run(12345)", comp.run(12345))
      _ <- show("kLoc.run((42, \"ignored\"))", kLoc.run(42 -> "ignored"))
      _ <- show("kMap.run(10)", kMap.run(10))
      _ <- show("kFlat.run(10)", kFlat.run(10))
      _ <- show("kMapK.run(8)", kMapK.run(8))
      _ <- show("split.run((2, \"abcd\"))", split.run(2 -> "abcd"))
      _ <- show("bothSides.run(Left(10))", bothSides.run(Left(10)))
      _ <- show("bothSides.run(Right(\"abc\"))", bothSides.run(Right("abc")))
      _ <- show("choose.run(Left(2))", choose.run(Left(2)))
      _ <- show("choose.run(Right(\"hello\"))", choose.run(Right("hello")))
      _ <- alwaysFail.run(42).attempt.flatMap {
        case Left(e) => IO.println(s"failed as expected: ${e.getMessage}")
        case Right(v) => IO.println(s"unexpected success: $v")
      }

      // 3) or with handleErrorWith / redeem
      _ <- alwaysFail.run(99).handleErrorWith(e => IO.println(s"handled: ${e.getMessage}"))
      _ <- alwaysFail.run(1).redeem(
        e => s"fallback for ${e.getMessage}",
        s => s"success: $s"
      ).flatMap(IO.println)
    } yield cats.effect.ExitCode.Success
}