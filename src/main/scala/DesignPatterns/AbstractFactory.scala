/*
 *
 *  * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package DesignPatterns

object AbstractFactory extends Enumeration {
  type AbstractCarFactory = Value
  val Chevy, Ford, Toyota, Honda, Tesla, Bla = Value

  trait Car

  class Chevy extends Car

  class Ford extends Car

  class Toyota extends Car

  class Honda extends Car

  class Tesla extends Car

  def apply(carType: AbstractFactory.AbstractCarFactory): Option[Car] = {
    carType match {
      case Chevy => Some(new Chevy)
      case Ford => Some(new Ford)
      case Toyota => Some(new Toyota)
      case Honda => Some(new Honda)
      case Tesla => Some(new Tesla)
      case _ => None
    }
  }
}


