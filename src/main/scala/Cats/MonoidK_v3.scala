package Cats

object MonoidK_v3 {
  import cats.MonoidK
  import cats.arrow.FunctionK
  import cats.syntax.all.*

  final case class Box[A](value: A)

  // --- Our custom target container G[_] with MonoidK
  final case class CSV[A](values: Vector[A])

  object CSV:
    given MonoidK[CSV] with
      def empty[A]: CSV[A] = CSV(Vector.empty)
      def combineK[A](x: CSV[A], y: CSV[A]): CSV[A] = CSV(x.values ++ y.values)

  // --- FunctionK instances to homogenize different F[_] into chosen G[_]

  // To List
  given optionToList: FunctionK[Option, List] with
    def apply[A](fa: Option[A]): List[A] = fa.toList

  given vectorToList: FunctionK[Vector, List] with
    def apply[A](fa: Vector[A]): List[A] = fa.toList

  given boxToList: FunctionK[Box, List] with
    def apply[A](fa: Box[A]): List[A] = List(fa.value)

  // To Option (choose a policy; here Vector -> headOption, Box -> Some)
  given optionToOption: FunctionK[Option, Option] with
    def apply[A](fa: Option[A]): Option[A] = fa

  given vectorToOption: FunctionK[Vector, Option] with
    def apply[A](fa: Vector[A]): Option[A] = fa.headOption

  given boxToOption: FunctionK[Box, Option] with
    def apply[A](fa: Box[A]): Option[A] = Some(fa.value)

  // To CSV (collect everything)
  given optionToCSV: FunctionK[Option, CSV] with
    def apply[A](fa: Option[A]): CSV[A] = CSV(fa.toVector)

  given vectorToCSV: FunctionK[Vector, CSV] with
    def apply[A](fa: Vector[A]): CSV[A] = CSV(fa)

  given boxToCSV: FunctionK[Box, CSV] with
    def apply[A](fa: Box[A]): CSV[A] = CSV(Vector(fa.value))

  // --- A tiny wrapper that “pre-applies” the natural transformation into G[_]
  final case class PackedTo[G[_], A](ga: G[A])

  object PackedTo:
    // Usage: PackedTo.to[List](fa), PackedTo.to[Option](fa), PackedTo.to[CSV](fa)
    def to[G[_]] = new ToPartiallyApplied[G]

    final class ToPartiallyApplied[G[_]]:
      def apply[F[_], A](fa: F[A])(using fk: FunctionK[F, G]): PackedTo[G, A] =
        PackedTo(fk(fa))

  // --- Generic combine for a heterogenous list already packed to G[_]
  def combinePacked[G[_] : MonoidK, A](xs: List[PackedTo[G, A]]): G[A] =
    xs.foldLeft(MonoidK[G].empty[A]) { (acc, p) => acc <+> p.ga }

  @main def heteroMonoidKDemo(): Unit =
    // 1) Gather heterogenous containers into List[Int]
    val asList: List[PackedTo[List, Int]] = List(
      PackedTo.to[List](Option(1)),
      PackedTo.to[List](Vector(2, 3)),
      PackedTo.to[List](Box(4))
    )
    val gathered: List[Int] = combinePacked[List, Int](asList)
    println(s"List target  -> $gathered") // List(1, 2, 3, 4)

    // 2) First-success semantics by homogenizing to Option[String]
    val asOption: List[PackedTo[Option, String]] = List(
      PackedTo.to[Option](Option.empty[String]),
      PackedTo.to[Option](Vector("A", "B")), // headOption => Some("A")
      PackedTo.to[Option](Box("C")) // Some("C") but never reached
    )
    val first: Option[String] = combinePacked[Option, String](asOption)
    println(s"Option target -> $first") // Some(A)

    // 3) Custom case-class target CSV[String] using our MonoidK
    val asCSV: List[PackedTo[CSV, String]] = List(
      PackedTo.to[CSV](Option("alpha")),
      PackedTo.to[CSV](Vector("beta", "gamma")),
      PackedTo.to[CSV](Box("delta"))
    )
    val csv: CSV[String] = combinePacked[CSV, String](asCSV)
    println(s"CSV target    -> ${csv.values.mkString(",")}") // alpha,beta,gamma,delta
}
