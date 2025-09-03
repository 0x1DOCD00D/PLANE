package scala3Features.ProductSerializable

object PS_v3 {
  def diff(a: Product, b: Product): List[(String, Any, Any)] =
    require(a.productPrefix == b.productPrefix, "Not the same product type.")
    val n = a.productArity
    require(n == b.productArity, "Different arity.")
    (0 until n).flatMap { i =>
      val name = a.productElementName(i)
      val av = a.productElement(i)
      val bv = b.productElement(i)
      if av == bv then Nil else List((name, av, bv))
    }.toList

  final case class User(id: String, age: Int, score: Double)
  final case class User1(id: String, age: Int, score: Double)

  @main def productDiffDemo(): Unit =
    val u1 = User("alice", 20, 0.0)
    val u2 = User("alice", 21, 99.5)
    val u3 = User1("bob", 21, 99.5)
    diff(u1, u2).foreach { case (k, v1, v2) => println(s"$k: $v1 -> $v2") }
    diff(u1, u3).foreach { case (k, v1, v2) => println(s"$k: $v1 -> $v2") }
}
