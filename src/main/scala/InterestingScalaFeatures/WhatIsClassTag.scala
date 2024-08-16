package InterestingScalaFeatures

class XXX
object XXX

object WhatIsClassTag:
  import scala.reflect.{ClassTag, classTag}
  
  def lookup[T :ClassTag]: Class[?] = classTag[T].runtimeClass

  @main def runClassTag(): Unit =
    println(lookup[Givens.ChainGivens.type])
    println(lookup[XXX.type])
    println(lookup[XXX.type].getName.contains(XXX.getClass.getName))
    println(XXX.getClass.getName)


