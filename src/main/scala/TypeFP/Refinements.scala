package TypeFP

object Refinements:
  type WithLegs = {def legs: Int}

  type PlusTwo[T <: Int] = scala.compiletime.ops.int.+[2, T]

  import scala.compiletime.ops.int.*


  import scala.compiletime.{erasedValue, summonInline}
  import scala.compiletime.ops.int._

  type Less[X <: Int, Y <: Int] = X < Y

  inline def lessThan[X <: Int, Y <: Int]: Boolean =
    if erasedValue[Less[X, Y]] then
      true
    else
      false

  def onlyEvens[T](x: T)(using ev: T =:= Int): Boolean = x % 2 == 0

  trait GreaterThanZero {
    type T

    def value: T
  }

  def createPositiveInt(value: Int): GreaterThanZero {type T = Int} =
    new GreaterThanZero {
      type T = Int
      val value: Int = if (value > 0) value else throw new IllegalArgumentException("Must be greater than zero")
    }
  
  def main(args: Array[String]): Unit = {
    val spider: WithLegs = new {
      val legs = 8
    }
    onlyEvens(4) // Compiles
//    onlyEvens("4") // Fails
    createPositiveInt(-3)

    val a: PlusTwo[4] = 6
    val b: Boolean = lessThan[5, 10]
  }