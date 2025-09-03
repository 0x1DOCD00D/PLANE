package scala3Features.ProductSerializable

object PS_v4 {

  import scala.deriving.Mirror
  import scala.compiletime.erasedValue

  inline def valuesToTuple[Elems <: Tuple](values: List[Any]): Elems =
    inline erasedValue[Elems] match
      case _: EmptyTuple =>
        EmptyTuple.asInstanceOf[Elems]
      case _: (h *: t) =>
        val head = values.head.asInstanceOf[h]
        val tail = valuesToTuple[t](values.tail)
        (head *: tail).asInstanceOf[Elems]

  inline def patch[A <: Product](a: A, updates: (String, Any)*)
                                (using m: Mirror.ProductOf[A]): A =
    val u = updates.toMap
    val newValues =
      (0 until a.productArity).map { i =>
        val name = a.productElementName(i)
        u.getOrElse(name, a.productElement(i))
      }.toList

    val tuple: m.MirroredElemTypes = valuesToTuple[m.MirroredElemTypes](newValues)

    m.fromProduct(tuple)

  final case class Job(id: String, retries: Int, maxDelayMs: Long)

  @main def demo(): Unit =
    val j1 = Job("sync-42", 3, 5000)
    val j2 = patch(j1, "retries" -> 5, "maxDelayMs" -> 10_000L)
    println(j2) // Job(sync-42,5,10000)
}
