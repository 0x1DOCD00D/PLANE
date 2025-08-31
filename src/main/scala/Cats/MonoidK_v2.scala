package Cats

object MonoidK_v2 {

  import cats.{MonoidK}
  import cats.syntax.all.*

  // Combine a list of F[A] using MonoidK and <+>
  def combineAllK[F[_] : MonoidK, A](xs: List[F[A]]): F[A] =
    xs.foldLeft(MonoidK[F].empty[A])(_ <+> _)

  // First success with Option
  val firstSome: Option[Int] =
    combineAllK[Option, Int](List(None, Some(2), Some(3))) // Some(2)

  // Concatenate results with List
  val all: List[Int] =
    combineAllK[List, Int](List(List(1, 2), List(), List(3))) // List(1,2,3)

  // Foldable#foldK is a convenience when you already have a Foldable container
  val alsoFirstSome: Option[String] = List(None, Some("hi"), Some("ignored")).foldK


  def main(args: Array[String]): Unit = {
    println(s"First Some: $firstSome")
    println(s"All combined List: $all")
    println(s"Also First Some: $alsoFirstSome")
  }
}
