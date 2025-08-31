package Cats

import cats.Applicative

object ApplicativesCore {
  def f(i: Int, c: Char): String = s"i=$i, c=$c"

  val int: Option[Int] = Some(5)
  val fnOpt: Option[Char => String] = int.map(i => (c: Char) => f(i, c))
  // fnOpt: Some((c: Char) => "i=5, c=c")

  def main(args: Array[String]): Unit = {
    val cVal: Char = 'x'

    // call the function inside the Option with map
    val resOpt: Option[String] = fnOpt.map(fn => fn(cVal))
    // resOpt == Some("i=5, c=x")

    // or shorter
    val resOpt2 = fnOpt.map(_(cVal))
    println(resOpt2)

    val cOpt: Option[Char] = Some('y')

    // for-comprehension (clear and idiomatic)
    val combined1: Option[String] = for
      fn <- fnOpt
      c <- cOpt
    yield fn(c)
    // combined1 == Some("i=5, c=y")

    println(combined1)
    // using flatMap / map
    val combined2: Option[String] = fnOpt.flatMap(fn => cOpt.map(c => fn(c)))
    println(combined2)

    val combinedDirect: Option[String] = for
      i <- int
      c <- cOpt
    yield f(i, c)
    println(combinedDirect)

    import cats.syntax.all._ // mapN, ap, etc.

    val cOptZ: Option[Char] = Some('z')

    // mapN (very readable)
    val viaMapN: Option[String] = (int, cOpt).mapN(f) // Some("i=5, c=z")

    // or, using function-in-context + ap:
    val fnInContext: Option[Int => Char => String] =
      Applicative[Option].pure((i: Int) => (c: Char) => f(i, c))

    val viaAp: Option[String] = fnInContext.ap(int).ap(cOptZ)
    println(viaAp)
  }
}
