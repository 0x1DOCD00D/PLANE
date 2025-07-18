package TypeFP

object TaglessFinal {
  trait SomeWrapper[F[_], A] {
    def wrap(a: A): F[A]
  }

  given optionWrapper[A]: SomeWrapper[Option, A] with
    def wrap(a: A): Option[A] = Some(a)

  given listWrapper[A]: SomeWrapper[List, A] with
    def wrap(a: A): List[A] = List(a)

  object makeWrapped:
    final class PartiallyApplied[F[_]]:
      def apply[A](a: A)(using sw: SomeWrapper[F, A]): F[A] =
        sw.wrap(a)

    def apply[F[_]]: PartiallyApplied[F] = new PartiallyApplied[F]

  def main(args: Array[String]): Unit = {
    println("Tagless Final Example")

    makeWrapped[Option](7.18f) match {
      case Some(value) => println(s"Wrapped in Option: $value")
      case None => println("No value wrapped in Option")
    }

    makeWrapped[List](7) match {
      case h :: t => println(s"Wrapped in List: $h, rest: $t")
      case List() => println("No value wrapped in List")
    }
  }
}
