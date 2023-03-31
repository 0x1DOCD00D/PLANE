/*
 * Copyright (c) 2023 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Language

object WhatIsInCaseClass extends App {
  import scala.reflect.runtime.universe.*
  import scala.reflect.runtime.{currentMirror, universe as ru}

  def ccToMap(cc: Product) = {
    val f = cc.getClass.getDeclaredFields
    println(cc.getClass.getName)
    f.map(f => {
      f.setAccessible(true)
      println(s"Parameter: ${f.getName}, ${f.getAnnotatedType.getType.getTypeName}, ${f.get(cc)}")
      (f.getName, f.get(cc))
    }
    ).toMap
  }

  def getTypeTag[T: TypeTag] = typeTag[T].tpe.members.filter(_.isTerm).map(_.asTerm).filter(_.isVal).map(f => (f.name, f.info)).toList

  def getFields[T: TypeTag](cc: T) = cc.getClass.getDeclaredFields.map(f => (f.getName, f.getClass.getName)).toList

  case class ParameterObject(stringType: String, optionType: Option[String])

  //  println(getTypeTag[ParameterObject])

  case class Person(name: String, age: ParameterObject)
  case object SinglePerson
  val name: String = "namename"

  ccToMap(Person("John", ParameterObject("string", Some("option"))))
  ccToMap(SinglePerson)
//  ccToMap(name.asInstanceOf[Product])
/*

  def getTypeTag[T: ru.TypeTag] = ru.typeTag[T].tpe.members.filter(_.isTerm).map(_.asTerm).filter(_.isVal).map(f=>(f.name, f.info)).toList
  def getFields[T: ru.TypeTag](cc:T) = cc.getClass.getDeclaredFields.map(f=>(f.getName,f.getClass.getName)).toList

  case class ParameterObject(stringType: String, optionType: Option[String])

  println(getTypeTag[ParameterObject])

  case class Person(name: String, age: Int)
  println(getTypeTag[Person])
  println(getFields[Person](Person("John", 30)))
*/
}
