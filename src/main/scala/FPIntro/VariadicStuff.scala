/*
 * Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package FPIntro

import scala.reflect.ClassTag
import scala.compiletime.{erasedValue, summonFrom}

trait DistributionEntity
trait DialsEntity
object VariadicStuff {
  trait TypeInfo[T] {
    def name: String
    def typeStuff: Class[?]
  }

  object TypeInfo {
    given [T](using ct: ClassTag[T]): TypeInfo[T] with {
      def name: String = ct.runtimeClass.getSimpleName
      def typeStuff: Class[?] = ct.runtimeClass
    }
  }

  inline def typeName[T](using ti: TypeInfo[T]): String = summonFrom { case _: TypeInfo[T] =>
    ti.name
  }

  def determineType[E <: AnyVal | String | DialsEntity | DistributionEntity](
     elems: E*
  )(using ti: TypeInfo[E]): Unit = {
//    println(s"Type of container of E is: ${elems.getClass.getSimpleName} containing ${typeName[E]}")

//    println(s"Type of E is: ${typeName[E]}")
    ti.typeStuff match
      case c if c == classOf[Int]         => elems.foreach(e => println("Int: " + e))
      case c if c == classOf[String]      => elems.foreach(e => println("String: " + e))
      case c if c == classOf[List[?]]     => elems.foreach(e => println("List: " + e))
      case c if c == classOf[DialsEntity] => elems.foreach(e => println("DialsEntity: " + e))
      case c if c == classOf[DistributionEntity] =>
        elems.foreach(e => println("DistributionEntity: " + e))
      case _ => println("Unknown type")

//    elems.foreach(e => println(e))
  }

  @main def runVariadicStuff(args: String*): Unit =
    println(
       "File /Users/drmark/IdeaProjects/PLANE/src/main/scala/FPIntro/VariadicStuff.scala created at time 10:22AM"
    )
    determineType(new DistributionEntity {})
    determineType(new DialsEntity {})
    determineType(1, 2, 3)
    determineType("a", "b", "c")
    determineType(1.0, 2.0, 3.0)
    determineType(Seq(1, 2, 3)*)
    determineType(List("a", "b", "c")*)

}
