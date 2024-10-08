////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Variance

object AnimalKingdom:
  class Animal {
    def makeSound(): Unit = println("Some animal sound")
  }

  class Dog extends Animal {
    override def makeSound(): Unit = println("Bark")
  }

  class Cat extends Animal {
    override def makeSound(): Unit = println("Meow")
  }
  
  def processAnimals[T <: Animal](lst: List[T]): List[Animal] =
    new Dog :: lst

  // Contravariant class
  class Trainer[-A <: Animal] {
    def train(animal: A): Unit = {
      println(s"Training ${animal.getClass.getSimpleName}")
      animal.makeSound()
    }
  }

  val dogTrainer: Trainer[Dog] = new Trainer[Animal] // Contravariance allows this
  dogTrainer.train(new Dog()) // This is allowed because Trainer[Animal] can handle Dog

  val animalTrainer: Trainer[Animal] = new Trainer[Animal]
  animalTrainer.train(new Dog()) // This is allowed
  animalTrainer.train(new Cat()) // This is also allowed

  @main def runAnimalKingdom(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Variance/AnimalKingdom.scala created at time 9:40AM")
