package FPIntro

object EagerVsCallByName:
  def f(s: () => String): Unit =
    println("CS476")
    s()

  def main(args: Array[String]): Unit = {
    f { () => {
      println("Init")
      "howdy, Sam!"
    }
    }
  }
