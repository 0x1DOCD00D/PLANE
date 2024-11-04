package FPIntro

object LazyParadox {
  lazy val x: Int = {
    throw new Exception("ouch!!!")
    1
  }

  def main(args: Array[String]): Unit = {
    try
      LazyParadox.x
    catch
      case _ => println("got it!")
    LazyParadox.x
  }
}
