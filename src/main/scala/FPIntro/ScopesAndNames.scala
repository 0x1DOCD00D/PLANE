package FPIntro

object ScopesAndNames:
  def f(p1: Int): Int = {
    def g(p2:Int, p3:Int = 0): Int =
      p2 * 3 + p3
    g(p1+1)
  }

  def main(args: Array[String]): Unit = {
    class X
    val o = new X
    println(f(2))
  }