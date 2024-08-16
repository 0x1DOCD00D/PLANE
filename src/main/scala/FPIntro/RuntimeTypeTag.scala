package FPIntro

case class Person(name:String)

def testRuntimeTypeTag[T](input: T) =
  input match {
    case t if t.isInstanceOf[String] => "you gave me a String"
    case t if t.isInstanceOf[Person] => s"you gave me a person named ${t.toString}"
    case t if t.isInstanceOf[Int] => "you gave me an Int"
    case t if t.isInstanceOf[Array[Int]] => "you gave me an Array of ints"
    case _ => "I don't know what type that is!"
  }

object RuntimeTypeTag:
  @main def runTT(): Unit =
    println(testRuntimeTypeTag(Array(1, 2, 3)))
    println(testRuntimeTypeTag(Array(1, 2, "3")))
    println(testRuntimeTypeTag(1))
    println(testRuntimeTypeTag("Mark"))
    println(testRuntimeTypeTag(Person("Mark Grechanik")))
