package FPIntro

object Laziness:
  lazy val x: Int = {
      println("Computing x")
      42
    }

  lazy val fib: LazyList[Int] = 0 #:: 1 #:: fib.zip(fib.tail).map { case (a, b) => a + b }

  class A {
    val x = {
      println("initializing x in A")
      1
    }
    lazy val y = {
      println("initializing y in A")
      2
    }
  }

  class B extends A {
    override val x = 10
    override lazy val y = 20
  }

  var count = 0
  lazy val xx = {
    count += 1
    count
  }

  def callByName(param: => Int): Int = {
    println("Inside the function")
    param + param
  }

  def main(args: Array[String]): Unit = {
    println("Before accessing x")
    println(x) // Prints: "Computing x" and 42
    println(x) // Prints: 42 (side effect doesn't happen again)

    println(fib.take(10).toList) // Prints: List(0, 1, 1, 2, 3, 5, 8, 13, 21, 34)

    val b = new B
    val a = new A
    println(b.x) // Prints: 10
    println(b.y) // Prints: 20
//    println(a.y) // Prints: 2

    println(xx) // Prints: 1
    count = 10
    println(xx) // Prints: 1 (cached value)

    var aa = 5
    println(callByName({
      println("Evaluating aa"); aa
    }))

  }