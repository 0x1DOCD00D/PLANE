package InterestingScalaFeatures

import scala.language.reflectiveCalls

object BehaviorIntro:
  import scala.reflect.Selectable.reflectiveSelectable
  
  trait Animal {
    val name:String
    def sound(): String
  }

  class Dog extends Animal {
    val name:String = "dog"
    def sound(): String = "Bark"
  }

  type PetAnimal = Animal {
    def pet(): String
  }

  val petDog: PetAnimal = new Animal {
    def sound(): String = "Bark"

    val name:String = "happy dog"
    def pet(): String = "Happy Dog"
  }

  def main(args: Array[String]): Unit = {
    println(petDog.pet())
  }