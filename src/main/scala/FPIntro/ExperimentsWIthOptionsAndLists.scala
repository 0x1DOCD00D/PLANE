package FPIntro

object ExperimentsWIthOptionsAndLists:

  def myFoldMap[F[_], A,B](as: F[A])(f: A => B)(mb: Monoid[B]): B = ???

  trait Monoid[A]:
    def zero: A

    def op(x: A, y: A): A


  object Monoid:

    def apply[A](using m: Monoid[A]): Monoid[A] = m
    def myApply[A](m: Monoid[A]): Monoid[A] = m

    val intMyOwnMonoid: Monoid[Int] = new Monoid[Int] {
      def zero: Int = 0

      def op(x: Int, y: Int): Int = x + y
    }
    
    given intAddition: Monoid[Int] with
      def zero: Int = 0

      def op(x: Int, y: Int): Int = x + y

    given stringConcatenation: Monoid[String] with
      def zero: String = String("")

      def op(x: String, y: String): String = x + y

    given listConcatenation[A]: Monoid[List[A]] with
      def zero: List[A] = Nil

      def op(x: List[A], y: List[A]): List[A] = x ++ y


  trait Foldable[F[*]]:

    def foldRight[A, B](as: F[A])(z: B)(f: (A, B) => B): B

    def foldLeft[A, B](as: F[A])(z: B)(f: (B, A) => B): B

    def foldMap[A, B](as: F[A])(f: A => B)(mb: Monoid[B]): B =
      foldLeft(as)(mb.zero) { (b, a) =>
        mb.op(b, f(a))
      }

    def concatenate[A](as: F[A])(m: Monoid[A]): A =
      foldLeft(as)(m.zero)(m.op)


  object Foldable:

    def apply[F[_]](using f: Foldable[F]): Foldable[F] = f

    given listFoldable: Foldable[List] with

      def foldRight[A, B](as: List[A])(z: B)(f: (A, B) => B): B =
        as.foldRight(z)(f)

      def foldLeft[A, B](as: List[A])(z: B)(f: (B, A) => B): B =
        as.foldLeft(z)(f)


    given vectorFoldable: Foldable[Vector] with

      def foldRight[A, B](as: Vector[A])(z: B)(f: (A, B) => B): B =
        as.foldRight(z)(f)

      def foldLeft[A, B](as: Vector[A])(z: B)(f: (B, A) => B): B =
        as.foldLeft(z)(f)


    given optionFoldable: Foldable[Option] with

      def foldRight[A, B](as: Option[A])(z: B)(f: (A, B) => B): B =
        as match
          case Some(a) => f(a, z)
          case None => z

      def foldLeft[A, B](as: Option[A])(z: B)(f: (B, A) => B): B =
        as match
          case Some(a) => f(z, a)
          case None => z

  def compose2Functions[A, B, C](f: A => B)(g: B => C): A => C =
    (a: A) => g(f(a))

  def myFilter[T](opt: Option[T])(f: T => Boolean): Option[T] =
    opt match
      case Some(value) => f(value) match
        case true => Some(value)
        case false => None
      case None => None

  def myMap(opt: Option[String])(f: String => Double): Option[Double] =
    opt match
      case Some(value) =>
        try {
            value.toDouble
            } catch {
            case _: Throwable => return None
        }
        Some(f(value))
      case None => None

  def computeMean(list: List[Int]): Option[Double] =
    if list.isEmpty then None
    else Some(list.sum.toDouble / list.size)

  def expandListOfLists(l: List[Int]): List[List[Int]] =
    l.map(i => List(i-1, i, i + 2))

  def main(args: Array[String]): Unit = {
    val m = Monoid.myApply(Monoid.intMyOwnMonoid)
    val m1 = Monoid[Int]
    println("Zero identity "+ m.zero)

    val xs = List(1, 2, 3, 4)

    val sum =
      Foldable[List].concatenate(xs)(Monoid[Int])

    val stringified =
      Foldable[List].foldMap(xs)(_.toString)(Monoid[String])

    val optionSum =
      Foldable[Option].concatenate(Some(10))(Monoid[Int])

    println(sum)
    println(stringified)
    println(optionSum)

    val comp1: (Int => Int) => Int => Int = compose2Functions[Int, Int, Int] {
        (i: Int) => i + 1
    }
    val comp2 = comp1 {
        (i: Int) => i * 2
    }
    println(comp2(5))


    println(myFilter(Option(10): Option[Int])(_ > 5))
    println(myFilter(None: Option[Int])(_ < 5))

    val out1 = List(0,5, 10)
    val out2 = expandListOfLists(out1)
    println(out2)
    val out3 = out1.flatMap(i => List(i-1, i, i + 2))
    println(out3)

    val productLineResult = List(1, 2, 3).map(i => List(i-1, i, i + 2)).
      flatten.
      filter(i => i % 2 == 0).
      collect(i => i * 10).
      slice(2, 5).
      flatMap(i => List(i, i + 1)).
      filter(i => i % 3 == 0)
    println(productLineResult)

    val res = myMap(Some("123.45"))(s => s.toDouble * 2)
    println(res)

    computeMean(List(1, 2, 3, 4, 5)) match
      case Some(mean) => println(s"Mean: $mean")
      case None => println("Cannot compute mean of an empty list.")
  }

    computeMean(List()) match
      case Some(mean) => println(s"Mean: $mean")
      case None => println("Cannot compute mean of an empty list.")

    List(1, 2, 3, 4, 5) match {
      case Nil => println("Empty list")
      case head :: tail => println(s"Head: $head, Tail: $tail")
    }

    List(1) match {
      case Nil => println("Empty list")
      case head :: tail => println(s"Head: $head, Tail: $tail")
    }