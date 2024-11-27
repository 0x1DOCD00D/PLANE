
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

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