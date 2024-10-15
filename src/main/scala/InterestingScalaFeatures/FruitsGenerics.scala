package InterestingScalaFeatures

import scala.reflect.ClassTag

//https://users.scala-lang.org/t/generic-function-output-for-covariance-type-is-wrong-is-it-a-type-system-bug-or-expected/10281
object FruitsGenerics:
  trait Fruit {
    val name: String
  }

  case class Apple(override val name: String) extends Fruit

  case class Banana(override val name: String) extends Fruit

  def onlyT[T](list: List[Fruit]): List[T] = list.collect {
    {
      case x: T => x
    }
  }

  def onlyT2[T: ClassTag](list: List[Fruit]): List[T] = list.collect {
    {
      case x: T => x
    }
  }

  val fruitList: List[Fruit] = List(Apple("app1"), Apple("app2"), Apple("app3"), Apple("app4"), Banana("ban1"), Banana("ban2"), Banana("ban3"))

  def main(args: Array[String]): Unit = {
    val a: List[Banana] = onlyT[Banana](fruitList)
    val b: List[Banana] = fruitList.collect { case x: Banana => x }
    val c: List[Banana] = onlyT2[Banana](fruitList)
    println(a)
    println(b)
    println(c)
  }