package FPIntro

object PartialFuncExperiments:

  def processX(contextProcessor: Int => Int): Function1[Any, Int] =
    case v: String => contextProcessor(v.toInt)
    case v: Float => contextProcessor(v.toInt)
    case _ => 0

  def testMeth(s: String): String => Int => (Int => Int) => Int = ss => i => (f: Int => Int) => f(ss.toInt + i)


  @main def runFuncExperiemtn(): Unit =
    println(processX(x=>x+1)(123.45f))
    println(testMeth("mark")("200")(2)((x:Int)=>x+1))

