package FPIntro

import scala.reflect.ClassTag

object TypeErasureTag:
  //  No ClassTag available for T
  //  def createArray[T](length: Int, element: T) = new Array[T](length)
  def createArray[T: ClassTag](length: Int, element: T): Array[T] =
    val arr = new Array[T](length)
    if length > 0 then arr(0) = element
    arr

  @main def runTypeErasureTag():Unit =
    println(createArray(1, "howdy").toList.toString)