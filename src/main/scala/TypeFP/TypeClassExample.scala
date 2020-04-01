/*
 * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package TypeFP

/*
  We define the following hierarchy of types and
  we need to issue documents that describe these types
 */
class ParentType

class Child1Type extends ParentType

class Child2Type extends ParentType

/*
  So we introduce a trait that declares a method for issuing
  documentation for the type with which this trait is parameterized.
  All types that need to be documented will implement this trait and
  its method to issue the documentation for a given type
 */
trait DocumentationOfTheType[T] {
  def documentation(o: T): String
}

/*
  This is the object that keeps documentation for types that we introduce.
 */
object DocumentAllThat {

  //the method documentation is overridden with the type with which the
  //the trait is parameterized
  object Child1TypeDocumentation extends DocumentationOfTheType[Child1Type] {
    override def documentation(o: Child1Type): String = "This is child 1 of the parent: " + o.toString
  }

  object Child2TypeDocumentation extends DocumentationOfTheType[Child2Type] {
    override def documentation(o: Child2Type): String = "This is child 2 of the parent: " + o.toString
  }

}


object TypeClassExample extends App {
  //this is a helper method. it takes two arguments:
  //the object of the documented type and the other object, d
  //that contains the implementation of the type that is documented
  def documentThisType[T](o: T)(d: DocumentationOfTheType[T]): String = {
    d.documentation(o)
  }

  println(documentThisType(new Child1Type)(DocumentAllThat.Child1TypeDocumentation))
}
