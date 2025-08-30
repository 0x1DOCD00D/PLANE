package scala3Features.Context

//https://users.scala-lang.org/t/cant-pass-generic-method-as-context-function-argument-eta-expansion-is-not-working/11999
/*
* boo as a value has the shape [T] => (T ?=> Boolean).
* foo2 asks for c: T ?=> Boolean for a specific T.
* Unless you pin T (by explicit type args, a type ascription,
* or by making c polymorphic), the compiler refuses to guess and
* also refuses to start an implicit search with an undetermined T.
* It’s an inference limitation around “polymorphic method → context-function” adaptation, not your logic.
* */
object PassMethodAsContextArgument {
  def foo[T](c: T ?=> Boolean)(x: T): Boolean = c(using x)
  // Scala 3 does expected-type–driven synthesis for context functions.
  // If the expected type is T ?=> R and you supply an expression of type R,
  // the compiler lifts it to a context lambda that simply ignores the context.
  println(foo[String](true)("Hola"))
  //same as foo[String]((using _: String) => true)("Hola")
  println(foo[String]((_: String) ?=> true)("Hola"))
  println(foo[String]((s: String) ?=> s.nonEmpty)("Hola"))
  println(foo[Int](false)(123))

  def fooN[T](c: T => Boolean)(x: T): Boolean = c(x)

//  fooN[String](true)("Hola") // does NOT compile
  println(fooN[String]((x:String)=>true)("Hola"))

  def boo[T](using x: T): Boolean = false

//  println(foo(boo)(1)) // error: cannot infer type T for ?=> Boolean
// Give T explicitly
  println(foo[Int](boo[Int])(1))

  def foo2[T](x: T)(c: T ?=> Boolean): Boolean = c(using x)

  def main(args: Array[String]): Unit = {
    println(foo2(1)(boo[Int]))
    // Eta-expand/instantiate to a value first
    val booInt: Int ?=> Boolean = boo[Int]
    println(foo(booInt)(1))

    // Make boo monomorphic
    def booI(using Int): Boolean = false

    println(foo(booI)(1)) // T inferred as Int from c
  }
}
