package InterestingScalaFeatures

import scala.language.reflectiveCalls

object BehaviorIntro:
  import scala.reflect.Selectable.reflectiveSelectable
  
  trait Animal {
    def sound(): String
  }

  class Dog extends Animal {
    def sound(): String = "Bark"
  }

  type PetAnimal = Animal {
    def pet(): String
  }

  val petDog: PetAnimal = new Animal {
    def sound(): String = "Bark"

    def pet(): String = "Happy Dog"
  }

  def main(args: Array[String]): Unit = {
    println(petDog.pet())
  }