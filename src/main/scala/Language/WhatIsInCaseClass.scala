/*
 * Copyright (c) 2023 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Language

object WhatIsInCaseClass:

  def ccToMap(cc: Product): Map[String, Any] =
    val fields = cc.getClass.getDeclaredFields
    println(cc.getClass.getName)
    fields.map { f =>
      f.setAccessible(true)
      println(s"Parameter: ${f.getName}, ${f.getAnnotatedType.getType.getTypeName}, ${f.get(cc)}")
      (f.getName, f.get(cc))
    }.toMap

  def getFields[T](cc: T): List[(String, String)] =
    cc.getClass.getDeclaredFields.map(f => (f.getName, f.getType.getName)).toList

  case class ParameterObject(stringType: String, optionType: Option[String])
  case class Person(name: String, age: ParameterObject)
  case object SinglePerson

  val name: String = "namename"

  @main def runWhatIsInCaseClass(args: String*): Unit =
    ccToMap(Person("John", ParameterObject("string", Some("option"))))
    ccToMap(SinglePerson)
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Language/WhatIsInCaseClass.scala created at time 10:35 AM")