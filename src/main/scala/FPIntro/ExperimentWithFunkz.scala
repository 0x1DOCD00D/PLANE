package FPIntro

object ExperimentWithFunkz:
  val a = 10
  class MyFunc extends Function[Int, Int]:
    override def apply(v1: Int): Int = if v1 > 0 then v1 - 3 else v1 + 2

  val f: Int => Int = (i: Int) => if i > 0 then i - 3 else i + a
  val _f = (i:Int)=>f(f(i))
  val _ff = (i:Int) => _f(_f(i))
  val _fff = (i:Int) => _f(_f(_f(i)))
  val theSame = (i:Int) => _fff((identity(i)))

  //  write a method that takes a function as the input and
//  the other input is how many times to compose this function
//  with itself and then compose it as many times as the parameter tells us

  def composeManyTimes(g: Int => Int, howMany: Int ): Int => Int =
    if howMany == 0 then (x:Int)=>x
    else (p:Int) =>composeManyTimes(g,howMany-1)(g(p))

// val i1 = p => composeManyTimes(g, 2)(g(p))
// val i2 = p => i1(composeManyTimes(g, 1)(g(p)))
// val i3 = p => i2(composeManyTimes(g, 0)(g(p)))
// val4p => i3((x:Int)=>x(g(p)))

  def returnFunction(i: Int => Int): Int => Int = (p: Int) =>i(i(p))

//  def composer(howMany: Int): Int => Int =


  def main(args: Array[String]): Unit = {
    println(f(-5))
    println(new MyFunc().apply(-5))
    println(returnFunction((i: Int) => if i > 0 then i - 3 else i + 2)(-2))
    println(_fff(-50))
    println(composeManyTimes((i:Int)=>i+1, 10)(1))
  }
