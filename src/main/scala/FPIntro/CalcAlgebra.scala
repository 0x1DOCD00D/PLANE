package FPIntro

type Environment = Map[String, Int]

enum CalcAlgebra[T]:
    case Value(x: T)
    case Variable(name: String)
    case Add(x: T, y: T)
    case Subtract(x: T, y: T)
    case Multiply(x: T, y: T)

    def eval(using num: Numeric[T]): T = this match
      case Add(x, y) => num.plus(x, y)
      case Subtract(x, y) => num.minus(x, y)
      case Multiply(x, y) => num.times(x, y)

object CalcAlgebra:
  given numericInt: Numeric[Int] with
    def plus(x: Int, y: Int): Int = x + y

    def minus(x: Int, y: Int): Int = x - y

    def times(x: Int, y: Int): Int = x * y

    def negate(x: Int): Int = -x

    def fromInt(x: Int): Int = x

    override def parseString(str: String): Option[Int] = ???

    override def toInt(x: Int): Int = ???

    override def toLong(x: Int): Long = ???

    override def toFloat(x: Int): Float = ???

    override def toDouble(x: Int): Double = ???

    override def compare(x: Int, y: Int): Int = ???


  def main(args: Array[String]): Unit = {
    val add = Add(10, 20)
    val sub = Subtract(10, 20)
    val mul = Multiply(10, 20)

    val complexExp = Add(Multiply(10, 20), Subtract(100, 50))

    println(add)
    println(sub)
    println(mul)
    println(complexExp)
    
    println(add.eval)
    println(sub.eval)
    println(mul.eval)
//    println(complexExp.eval)
  }