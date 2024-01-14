/*
 * Copyright (c) 2023 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package InterestingScalaFeatures

object CompareContainers {

  trait Container[A <: Container[A]] extends Ordered[A]
  class FruitContainer extends Container[FruitContainer] {
    override def compare(that: FruitContainer): Int = 0
  }
  class VeggieContainer extends Container[VeggieContainer] {
    override def compare(that: VeggieContainer): Int = 2
  }

  class ShadyContainer extends Container[VeggieContainer] {
    override def compare(that: VeggieContainer): Int = ???
  }


  def main(args: Array[String]): Unit = {
    val fruitContainer = new FruitContainer
    val veggieContainer = new VeggieContainer
    //println(fruitContainer.compare(veggieContainer)) //This will not compile
    println(fruitContainer.compare(fruitContainer)) //But this will
    println(veggieContainer.compare(veggieContainer)) //And this will too now
  }
}