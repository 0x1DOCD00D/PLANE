package TypeFP

object ExpressionDSLTaglessWithMonad {
  trait Monad[F[_]]:
    def pure[A](a: A): F[A]
    extension [A](fa: F[A])
      def map[B](f: A => B): F[B]
      def flatMap[B](f: A => F[B]): F[B]

  trait ExprDsl[F[_]]:
    def lit(n: Int): F[Int]
    def add(x: F[Int], y: F[Int]): F[Int]

  type Id[A] = Either[String, A]
  given Monad[Id] with
    def pure[A](a: A): Id[A] =
      Right(a)
    extension [A](fa: Id[A])
      def map[B](f: A => B): Id[B] =
        fa match
          case Right(value) => Right(f(value))
          case Left(err) => Left(err)
      def flatMap[B](f: A => Id[B]): Id[B] =
        fa match
          case Right(value) => f(value)
          case Left(err) => Left(err)

  type Container[A] = A
  given Monad[Container] with
    def pure[A](a: A): A = a

    extension [A](fa: A)
      def map[B](f: A => B): B = f(fa)
      def flatMap[B](f: A => B): B = f(fa)

  given evalDsl: ExprDsl[Container] with
    def lit(n: Int): Int = n
    def add(x: Int, y: Int): Int = x + y

  case class Pretty[A](render: String)

  given Monad[Pretty] with
    def pure[A](a: A): Pretty[A] = Pretty(a.toString)

    extension [A](fa: Pretty[A])
      def map[B](f: A => B): Pretty[B] = Pretty(fa.render)
      def flatMap[B](f: A => Pretty[B]): Pretty[B] = Pretty(fa.render)

  given printDsl: ExprDsl[Pretty] with
    def lit(n: Int): Pretty[Int] = Pretty(n.toString)
    def add(x: Pretty[Int], y: Pretty[Int]): Pretty[Int] =
      Pretty("(" + x.render + " + " + y.render + ")")

  given evalDslEither: ExprDsl[Id] with
    def lit(n: Int): Id[Int] =
      Right(n)
    def add(x: Id[Int], y: Id[Int]): Id[Int] =
      (x, y) match
        case (Right(a), Right(b)) => Right(a + b)
        case (Left(err), _) => Left(err)
        case (_, Left(err)) => Left(err)

  def program[F[_]](using D: ExprDsl[F], M: Monad[F]): F[Int] =
    val d = summon[ExprDsl[F]]
    val M = summon[Monad[F]]
    import M.* // brings map and flatMap into scope
    for
      sum <- D.add(D.lit(2), D.lit(3))
      sumF  = M.pure(sum)
      tot <- d.add(d.lit(1), sumF)
    yield tot

  def main(args: Array[String]): Unit = {
    val resEval = program[Container]
    println("Evaluated to " + resEval)

    val resPrint: Pretty[Int] = program[Pretty]
    println("Printed as " + resPrint.render)

    program[Id] match
      case Right(value) => println("Evaluated to " + value)
      case Left(err) => println("Evaluation error " + err)
  }
}