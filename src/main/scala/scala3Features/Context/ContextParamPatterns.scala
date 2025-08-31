package scala3Features.Context

object ContextParamPatterns {
  // 1) Context lambda syntax
  val alwaysTrue: String ?=> Boolean = (_: String) ?=> true

  given String = "given String"
  // 2) Supplying context explicitly
  def foo[T](c: T ?=> Boolean)(x: T): Boolean = c(using x)

  // 3) Turning a using-method into a value
  def boo[T](using T): Boolean = false

  def main(args: Array[String]): Unit = {
    println(foo[String](true)("Hola")) // auto-lift to a context lambda
    println(alwaysTrue)
    println(foo[String]((s: String) ?=> s.nonEmpty)("x")) // explicit context lambda
    println(foo[Int](false)(123)) // explicit value
    val booInt: Int ?=> Boolean = boo[Int]
    println(foo(booInt)(1))
  }
}
