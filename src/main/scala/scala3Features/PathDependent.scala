package scala3Features

object PathDependent:
  abstract class Box {
    type T
    var value: T
  }

  class BoxManager {
    def createIntBox(): Box {type T = Int} = new Box {
      type T = Int
      var value:T = 0
    }

    def createStringBox(): Box {type T = String} = new Box {
      type T = String
      var value:T = ""
    }
  }

  val manager = new BoxManager

  val intBox: Box {type T = Int} = manager.createIntBox()
  val stringBox: Box {type T = String} = manager.createStringBox()

  intBox.value = 42 // OK: intBox.value is of type Int
  stringBox.value = "Hi" // OK: stringBox.value is of type String

  def main(args: Array[String]): Unit = {
    val x: intBox.T = 20
//    val y: intBox.T = ""
    println(x)
  }
