package FPIntro

object BlocksOfCode:
  lazy val bOC = {
    val v1: Int = 5
    def incVar(i:Int): Int = i + 1
    println(incVar(v1)+ v1)
  }

  def passBlockOfCode(block: => Unit): Unit = {
    block
  }

  def IFF(cond: => Boolean)(forTrue: => Unit)(forFalse: => Unit): Unit =
    if( cond == true) forTrue
    else forFalse

  def main(args: Array[String]): Unit = {
    println("Happy happy campers")
    val inp = 2
    IFF( inp == 2 ) {
      println("true")
    } {
      println("false")
    }

    println {
      passBlockOfCode {
        val v1: Int = 5

        def incVar(i: Int): Int = i + 1

        println(incVar(v1) + v1)
      }
    }
//    println(bOC)
  }
