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

object BridgePattern {

  trait SomeGeneralAbstraction {
    def behavior: Boolean
  }

  trait ConcreteChild1 extends SomeGeneralAbstraction {
    override def behavior: Boolean = true
  }

  trait ConcreteChild2 extends SomeGeneralAbstraction {
    override def behavior: Boolean = false
  }

  //we have a type hierarchy that models something and it uses the abstraction and its
  //implementations above
  abstract class Model {
    this: SomeGeneralAbstraction =>
    def DoIt: Boolean
  }

  class RefinedModel1 extends Model with ConcreteChild1 {
    override def DoIt: Boolean = behavior
  }

  class RefinedModel2 extends Model with ConcreteChild2 {
    override def DoIt: Boolean = behavior
  }

}
