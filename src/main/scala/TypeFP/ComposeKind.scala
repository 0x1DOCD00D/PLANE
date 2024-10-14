package TypeFP

object ComposeKind:
  // Define a type constructor that composes `F` and `G` (both kind `* → *`)
  trait Compose[F[_], G[_]] {
    type Composed[A] = F[G[A]]
  }

  // Compose `Option` and `List` into a new type constructor
  object ComposeOptionList extends Compose[Option, List] {
    // `Composed[Int]` is `Option[List[Int]]`
    val example: Composed[Int] = Some(List(1, 2, 3))
  }

  // `example` is `Some(List(1, 2, 3))`
  def main(args: Array[String]): Unit = {
    val composeInstance = new Compose[List, Set] {}
    val example: composeInstance.Composed[Int] = List(Set(1, 2, 3), Set(4, 5, 6))
  }