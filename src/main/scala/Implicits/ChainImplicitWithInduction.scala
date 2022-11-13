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


/*
* In object ChainImplicits we showed how to chain implicits using intermediate type classes and implicit methods.
* However, we must introduce as many implicit conversion methods as the number of the intermediate types.
* These intermediate types, however, are connected via some type resolution relation, we can call it NextType.
* */

object ChainImplicitWithInduction extends App {

  /*
    * class BaseClass(val i: Int) - it wraps the value of the base type, int
    * implicit def convert2BaseClass(i: Int): BaseClass = new BaseClass(i)
    * implicit def convert2BaseClass(i:SomeType):BaseClass = new BaseClass(i)
    * Now we have an inductive step
    * class IntermediateClass_N+1
    * implicit def convert2N+1FromN[XX](i:XX)(implicit xx: XX=>IntermediateClass_N):IntermediateClass_N+1 = new IntermediateClass_N+1
  */

  //  we define our overarching type for all intermediate types
  trait IntermediateType

  //A concrete base type is a class that will be instantiated implicitly to take the input value of some type, T
  class BaseClass[T](i: T) extends IntermediateType

  //this class, NextType specifies a relation between some type used as the type parameter and the parameterized type, NextType
  class NextType[NT <: IntermediateType] extends IntermediateType

  type IntermediateType_1 = NextType[BaseClass[_]]
  type IntermediateType_2 = NextType[IntermediateType_1]
  type IntermediateType_3 = NextType[IntermediateType_2]
  type IntermediateType_4 = NextType[IntermediateType_3]
  type IntermediateType_5 = NextType[IntermediateType_4]

  //  adding new types using the relation NextType can go on forever...

  implicit def convert2BaseClass[T](i: T): BaseClass[T] = new BaseClass(i)

  implicit def inductionStep[T, D <: IntermediateType](i: T)(implicit typeRelation: T => D): NextType[D] = new NextType[D] {}

  val result: IntermediateType_5 = 3
}
