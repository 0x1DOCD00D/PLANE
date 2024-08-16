/*
 * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package TypeFP

class TypeClassExampleWithViewBounds extends App {
  def documentation[T: DocumentationOfTheType](o: T): String = implicitly[DocumentationOfTheType[T]].documentation(o)

  implicit object Child1TypeDocumentation extends DocumentationOfTheType[Child1Type] {
    override def documentation(o: Child1Type): String = "This is child 1 of the parent"
  }

  implicit object Child2TypeDocumentation extends DocumentationOfTheType[Child2Type] {
    override def documentation(o: Child2Type): String = "This is child 2 of the parent"
  }

  implicit class DocumentationOperation[T: DocumentationOfTheType](o: T) {
    def documentThisType: String = documentation(o)
  }

  println((new Child1Type).documentThisType)

}
