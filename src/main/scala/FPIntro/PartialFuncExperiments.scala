package FPIntro

object PartialFuncExperiments:
  def pf(name: String, f: Any => Unit): PartialFunction[Any, Unit] = {
    case n if n == name => f(n)
    case throwAwayObject => println(s"to prevent an exception: $throwAwayObject")
  }

  def processX(contextProcessor: Int => Int): Function1[Any, Int] =
    case v: String => contextProcessor(v.toInt)
    case v: Float => contextProcessor(v.toInt)
    case _ => 0

  def testMeth(s: String): String => Int => (Int => Int) => Int = ss => i => (f: Int => Int) => f(ss.toInt + i)


  @main def runFuncExperiemtn(): Unit =
    println(processX(x=>x+1)(123.45f))
    println(testMeth("mark")("200")(2)((x:Int)=>x+1))
    val f: PartialFunction[Any, Unit] = pf("Mark", (p:Any)=>{println(p)})
    f("Mark")
    f("SomeRandomName")

