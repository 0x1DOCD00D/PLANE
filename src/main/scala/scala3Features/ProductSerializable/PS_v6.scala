package scala3Features.ProductSerializable

object PS_v6 {

  import java.security.MessageDigest
  import java.nio.charset.StandardCharsets

  def stableKey(p: Product): String =
    val sb = new StringBuilder(p.productPrefix).append('(')
    val n = p.productArity
    var i = 0
    while i < n do
      if i > 0 then sb.append(',')
      sb.append(p.productElementName(i)).append('=').append(p.productElement(i))
      i += 1
    sb.append(')')
    val md = MessageDigest.getInstance("SHA-256")
    md.digest(sb.toString.getBytes(StandardCharsets.UTF_8)).map("%02x".format(_)).mkString

  def memoizeByProduct[A <: Product & Serializable, B](f: A => B): A => B =
    val cache = new java.util.concurrent.ConcurrentHashMap[String, B]()
    (a: A) =>
      val k = stableKey(a)
      cache.computeIfAbsent(k, _ => f(a))

  final case class PlanParams(cpus: Int, memGb: Int, spot: Boolean) // Product

  @main def memoDemo(): Unit =
    val costly: PlanParams => Double = p =>
      println(s"Computing for $p")
      // pretend expensive simulation
      p.cpus * 1.5 + p.memGb * 0.8 + (if p.spot then -2.0 else 0.0)

    val memo = memoizeByProduct(costly)
    println(memo(PlanParams(8, 32, spot = true)))
    println(memo(PlanParams(8, 32, spot = true))) // cached, no "Computing..." line
}
