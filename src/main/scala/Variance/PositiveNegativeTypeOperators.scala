////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Variance

object PositiveNegativeTypeOperators:
  class Animal {
    def makeSound(): Unit = println("Some animal sound")
  }

  class Dog extends Animal {
    override def makeSound(): Unit = println("Bark")
  }

  // Covariant Function: Covariant types occur in the range (output) of functions
  class AnimalProducer[+A](animal: A) {
    def produce: A = animal // A is in the return type (output)
  }

  val dogProducer: AnimalProducer[Dog] = new AnimalProducer(new Dog)
  val animalProducer: AnimalProducer[Animal] = dogProducer // This works because of covariance

  val producedAnimal: Animal = animalProducer.produce

  // Contravariant Function: Contravariant types occur in the domain (input) of functions
  class AnimalTrainer[-A <: Animal] {
    def train(animal: A): Unit = animal.makeSound() // A is in the input (domain)
  }

  val animalTrainer: AnimalTrainer[Animal] = new AnimalTrainer[Animal]
  val dogTrainer: AnimalTrainer[Dog] = animalTrainer // This works because of contravariance

  dogTrainer.train(new Dog())

  @main def runPositiveNegativeTypeOperators(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Variance/PositiveNegativeTypeOperators.scala created at time 9:59AM")
