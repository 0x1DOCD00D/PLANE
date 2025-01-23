
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scala3Features

//https://users.scala-lang.org/t/overriding-the-equals-method-leads-to-values-being-assigned-an-incorrect-type/10520/3
object TypesAndThings:
  enum Type:
    case Num
    case Bool

  enum TypeRepr[A <: Type]:
    case Num extends TypeRepr[Type.Num.type]
    case Bool extends TypeRepr[Type.Bool.type]

  def to[A <: Type](repr: TypeRepr[A]): A = repr match
    case _: TypeRepr.Bool.type => Type.Bool
    case _: TypeRepr.Num.type => Type.Num

  object NestedType:
    val nested = Some(Some(3))

    def nestedMatch(value: Any): String =
      value match
        case _:nested.type => "It is exactly Some(Some(3))"
        case _ => "No match"

  @main def runEqualAndThings(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/scala3Features/TypesAndThings.scala created at time 4:17PM")
    val r1: Type.Num.type = to(TypeRepr.Num)
    println(r1)

    val r2: Type.Bool.type = to(TypeRepr.Bool)
    println(r2)
    
    println(NestedType.nestedMatch("howdy!"))
    println(NestedType.nestedMatch(NestedType.nested))
    

