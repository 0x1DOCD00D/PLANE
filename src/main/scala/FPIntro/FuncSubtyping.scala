package FPIntro

object FuncSubtyping:
  trait Ints
  trait Evens extends Ints
  trait Primes extends Ints

  val e: Ints => Evens = ???
  val p: Primes => Ints = ???

  def takeFunc0(f: Ints => Evens)(p: Evens): Ints = f(p)
  def takeFunc1(f: Primes => Ints): Int = ???

  def main(args: Array[String]): Unit = {
    takeFunc0(e)(new Evens {})
//    takeFunc0(p)(new Primes {})
    takeFunc1(e)
    takeFunc1(p)
  }