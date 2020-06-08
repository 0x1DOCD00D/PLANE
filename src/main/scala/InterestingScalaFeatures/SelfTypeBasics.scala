/*
 *
 *  * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package InterestingScalaFeatures

object SelfTypeBasics extends App {

  trait A {
    def methodA = println("A")
  }

  trait B {
    //we need to access the method of A
    //private val someName = this
    someName: A =>
    def methodB = {
      println("B")
      methodA
    }
  }

  (new A with B).methodB
  (new A with B).methodA
  (new B with A).methodA
  (new A with B).methodB
  //(new A).methodA - a trait is abstract
  new A {}.methodA

  //(new B{}).methodA - it is illegal to access the method of A without using proper inheritance - what is inherited then?
  //(new B{}).methodB - same here, methodA is used in methodB

  //when mixing the trait B in, the trait A is required also
  class InhB extends B with A

  //we can mix the functionality differently
  class InhA extends A

  class InhB1 extends InhA with B

  (new InhB).methodA
  (new InhB).methodB
  (new InhB1).methodA
  (new InhB1).methodB
}
