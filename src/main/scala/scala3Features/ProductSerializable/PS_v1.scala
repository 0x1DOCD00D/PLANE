package scala3Features.ProductSerializable

object PS_v1 {
  /*
  * Make the intent explicit: “only product-like cases.”
  * Product is the marker/contract all tuples and case classes implement.
  * Putting it on the parent says “this ADT is a sum of products,” which is
  * exactly how we model algebraic data types. It also gives you generic
  * hooks like productArity, productElement(i), and productIterator for quick reflection-style utilities.
  * */
  sealed trait Event extends Product with Serializable
  final case class Click(x: Int, y: Int) extends Event
  case object Quit extends Event

  def csv(p: Product): String = {
    (0 until p.productArity).map(i => String.valueOf(p.productElement(i))).mkString(",") ++ " :: " ++
    (0 until p.productArity).map(p.productElementName(_)).mkString(",")
  }

  case class User(id: Int, name: String)

  def send[A <: Serializable](a: A): Unit = ()
  sealed trait Msg // not serializable at the type level
  final case class Update(i: Int) extends Msg // case classes are serializable
  val m: Msg = Update(1)
  // send(m)          // does not compile: Msg <: Serializable is false

  sealed trait Msg2 extends Product with Serializable
  final case class Update2(i: Int) extends Msg2

  val m2: Msg2 = Update2(1)
  send(m2) // compiles: Msg2 <: Serializable is true

  /*
  * redundant for the cases themselves: case classes/objects already extend Product and are serializable.
  * Adding it on the parent mostly constrains intent and helps with the type bounds example above.
  * Not a security or hard restriction: a non-case class could still extend your trait by manually
  * implementing Product. The point is convention and convenience, not enforcement.
  * Scala 3 note: if you use enum for ADTs, you typically don’t need this.
  * Scala 3 enums and case classes already behave like serializable products, and modern derivation
  * uses Mirror, not Product, so the extends Product with Serializable line is usually optional.
  * It remains harmless and sometimes useful when interacting with older code or APIs with Serializable bounds.
  * */

  def main(args: Array[String]): Unit =
    println(s"CSV: ${csv(Click(10, 20))}")
    val tup: (Int, String) = Tuple.fromProductTyped(User(1, "Mark"))
    println(s"Tup: $tup")


}
