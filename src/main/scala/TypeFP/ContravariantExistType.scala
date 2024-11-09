package TypeFP

import Variance.AnimalKingdom.*

object ContravariantExistType:
  
  class Consumer[-A <: Mammal] {
    private var storage: Option[Animal] = None
    
    def consume(value: Animal): Unit =
      storage = Option(value)  
      println(s"Consumed: $value")
    
    def produce(): Mammal = storage.getOrElse(throw Exception("nothing stored"))
  }

  // Define a method that accepts a consumer of some unknown type T
  def useConsumer(c: Consumer[? >: Dog]): Unit = {
    c.consume(new Dog) 
    c.consume(new Cat) 
    c.consume(new SmallDog) 
    c.consume(new Animal) 
  }
  
  def main(args: Array[String]): Unit = {
    val anyConsumer: Consumer[Dog] = new Consumer[Mammal]
    useConsumer(anyConsumer) // This works because Consumer is contravariant
    useConsumer(new Consumer[Animal]) // This works because Consumer is contravariant
    //  useConsumer(new Consumer[SmallDog]) // This does not work
   println(anyConsumer.produce()) 
  }