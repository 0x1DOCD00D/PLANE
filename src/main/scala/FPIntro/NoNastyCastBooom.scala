package FPIntro

object NoNastyCastBooom {
  val ai: Array[Int]= Array(1, 2, 3)
  val aa: Array[Any] = ai.asInstanceOf[Array[Any]]

  def main(args: Array[String]): Unit = {
//    aa(0) = "Hello"
//    println(ai(0)) // This will print "Hello" instead of 1, which
  }
}
