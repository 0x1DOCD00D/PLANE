/*
 * Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package DSLWorkshop

object EntityCreation:
  //create an Order where ( Container is 1, Description is "whiskey", Price is 100.0, Quantity is 2 )
  object create:
    infix def an(o: Entity) = o match {
      case Order => new OrderRecord()
      case _ => throw new Exception("Not supported")
    }

  class OrderRecord:
    infix def where(a: EntityAttribute) = a match {
      case IdAttribute(i) =>
        this.id = i
        this
      case DescriptionAttribute(s) =>
        this.description = s
        this
      case _ => throw new Exception("Not supported")
    }
    private var id: Int = 0
    private var description: String = ""
    override def toString: String = s"OrderRecord($id, $description)"
  sealed trait Entity
  case object Order extends Entity
  case object Inventory extends Entity

  sealed trait EntityAttribute:
    protected var attrs: Array[Any] = Array()
    infix def and(a: EntityAttribute): EntityAttribute = a match {
      case IdAttribute(i) => this
      case DescriptionAttribute(s) => this
      case _ => throw new Exception("Not supported")
    }
  case class IdAttribute(i: Int) extends EntityAttribute
  case class DescriptionAttribute(s: String) extends EntityAttribute
  object Id extends EntityAttribute:
    infix def is(i: Int): IdAttribute =
      val v = IdAttribute(i)
      attrs = attrs :+ v
      v
  object Description extends EntityAttribute:
    infix def is(s: String): DescriptionAttribute =
      val v = DescriptionAttribute(s)
      attrs = attrs :+ v
      v

  @main def runDslEntity: Unit =
    import EntityCreation.*
    create an Order where ((Id is 1) and (Description is "whiskey"))
    println(create an Order where ((Id is 2) and (Description is "carrots")))
