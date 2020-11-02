/*
 *
 *  * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package Implicits

object ChainImplicits extends App {

  //  This is a standard example of the immediate implicit conversion - no chaining is needed
  //  since the int 5 is wrapped into an instance of the class C1 as its parameter and then
  //  the method x is called with parameter 3
  implicit class C1(val i: Int) {
    def x(j: Int): Int = i * j
  }

  val result = 5 x 3
  println(result)

  //  Just for fun, we define the implicit conversion for integer power integer
  implicit class C2(val i: Int) {
    def ^^(j: Int): Int = {
      (0 until j).foldLeft(1)((acc, _) => acc * i)
    }
  }

  val result1 = 5 ^^ 3
  println(result1)

  // Our goal is to define a syntactic construct <int> ~>> <string> like 3 ~>> "Times" that outputs the string "Times 3"
  //  The method def ~>>(j:String) = j ++ " " ++ i.toString will go to the class C6
  //  We want to accomplish the conversion through a series of implicit conversions
  //  First, we create an explicit class called C3 that takes the integer value and we will define an implicit method that converts
  //  the input integer into an instance of C3
  class C3(val i: Int)

  implicit def convert2C3(i: Int): C3 = new C3(i)

  //  It is converted convert2C3(3)
  val result3: C3 = 3

  //  This intermediate conversion must continue until we reach an instance of C6 that provides the method ~>>
  /*
   * https://docs.scala-lang.org/tutorials/FAQ/chaining-implicits.html
   * According to the Scala spec,  "Scala has a restriction on automatic conversions to add a method, which is that it won’t apply more than one conversion in trying to find methods."
   * "However, if an implicit definition requires an implicit parameter itself, Scala will look for additional implicit values for as long as needed."
   * We introduce an intermediate type named C4 below and an implicit method that will return an instance of C4. The name of the type does not matter as long as there are no name clashes.
   * This method requires an implicit parameter we call xx that is a function that maps some type parameter, XX to C3 and the explicit parameter to this function is also of type XX.
   * Type inference algorithm kicks in here to determine the actual type substituted for the type parameter XX.
   * The Scala compiler must resolve the parameter implicit xx: XX=>C3 to find a function that converts a value of some type, XX to C3. This function is convert2C3(i:Int):C3,
   * so the resulting conversion is convert2C4FromC3(3)(convert2C3).
   * The same pattern is applied to reach the class C6. To make it more general, it can be written as the following.
   * class BaseClass(val i:SomeType)
   * where we think of BaseClass as IntermediateClass_0
   * implicit def convert2BaseClass(i:SomeType):BaseClass = new BaseClass(i)
   * Now we have an inductive step
   * class IntermediateClass_N+1
   * implicit def convert2N+1FromN[XX](i:XX)(implicit xx: XX=>IntermediateClass_N):IntermediateClass_N+1 = new IntermediateClass_N+1
  */

  class C4

  implicit def convert2C4FromC3[XX](i: XX)(implicit xx: XX => C3): C4 = new C4

  val result4: C4 = 3

  class C5

  implicit def convert2C5FromC4[XX](i: XX)(implicit xx: XX => C4): C5 = new C5

  val result5: C5 = 3

  class C6[T](val i: T) {
    def ~>>(j: String): String = j ++ " " ++ i.toString
  }

  implicit def convert2C6FromC5[XX](i: XX)(implicit xx: XX => C5): C6[XX] = new C6(i)

  val result6 = 3 ~>> "Times"
  println(result6)
}
