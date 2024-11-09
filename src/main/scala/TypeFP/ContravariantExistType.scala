package TypeFP

import Variance.AnimalKingdom.*

object ContravariantExistType:
  // Define a contravariant consumer class
  class Consumer[-A] {
    def consume(value: A): Unit = println(s"Consumed: $value")
  }

  // Define a method that accepts a consumer of some unknown type T
  def useConsumer(c: Consumer[? >: Dog]): Unit = {
    c.consume(new Dog) 
  }
  
  def main(args: Array[String]): Unit = {
    val anyConsumer: Consumer[Dog] = new Consumer[Mammal]
    useConsumer(anyConsumer) // This works because Consumer is contravariant
    useConsumer(new Consumer[Animal]) // This works because Consumer is contravariant
    //  useConsumer(new Consumer[SmallDog]) // This does not work
  }