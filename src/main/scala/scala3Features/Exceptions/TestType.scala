package scala3Features.Exceptions

//https://users.scala-lang.org/t/typetest-on-throw-exception/11998
import scala.reflect.TypeTest
import scala.language.experimental.saferExceptions

object TestType {
  def testThrows[E <: Exception](thunk: => Unit throws E)(using tt: TypeTest[Exception, E]): Unit =
    try
      thunk
    catch
      //case tt(e: E) => println("testThrows caught TypeTest NPE")
      case e: E => println("testThrows caught NPE")


  def testReturns[E <: Exception](thunk: => Exception)(using tt: TypeTest[Exception, E]): Unit =
    val ex: Exception = thunk
    ex match
      case e: E => println("testReturns matched NPE")
      case tt(e: E) => println("testReturn matched TypeTest NPE")


  def main(args: Array[String]): Unit =
    testThrows[NullPointerException]:
      throw new NullPointerException()
    testReturns[NullPointerException]:
      new NullPointerException()
}
