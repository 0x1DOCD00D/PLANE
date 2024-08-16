/*
 * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package TypeFP

object TypeClassExampleWithImplicits extends App {

  implicit object Child1TypeDocumentation extends DocumentationOfTheType[Child1Type] {
    override def documentation(o: Child1Type): String = "This is child 1 of the parent: " + o.getClass.toString
  }

  implicit object Child2TypeDocumentation extends DocumentationOfTheType[Child2Type] {
    override def documentation(o: Child2Type): String = "This is child 2 of the parent: " + o.getClass.toString
  }

  def documentThisType[T](o: T)(implicit d: DocumentationOfTheType[T]): String = {
    d.documentation(o)
  }

  println(documentThisType(new Child1Type))

}
