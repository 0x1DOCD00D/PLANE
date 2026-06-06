package FPIntro

object GameWithTypes {
  class Car
  val v: 5 | 6 | 7 | Int | Car | Boolean | true | Null = 5
  val insane: Nothing = throw new Exception
//  val v1: 5 | 6 | 7 = null
  def main(args: Array[String]): Unit = {
    println(v.getClass)
  }
}
