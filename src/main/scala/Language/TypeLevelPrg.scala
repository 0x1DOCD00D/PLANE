/*
 * Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Language

import scala.compiletime.ops.int.*
import scala.compiletime.ops.string.{CharAt, Length, Substring}

object TypeLevelPrg:
  val vt: 2 + 3 = 5
  def id[T](x: T): x.type = x

  val v1: 3 = id(3)
  val v2: 5 = id(5)
  val v3: v1.type + v2.type = id(v1 + v2)
  val v4: v1.type | v2.type = id(v1)
  val v5: v1.type * v2.type = id(v1 * v2)

  @main def runTypeLevelPrg(args: String*): Unit =
    printf("%s is %d\n", "name", 77)
    def fWithManyParms(x: Int, y: Int, z: Int): Int = x + y + z
    def fWithManyParmsAsTuple(x: (Int, Int, Int)): Int = x(0) + x(1) + x(2)
    // change of the syntax for the method call with tuples
    println(fWithManyParms(1, 2, 3) == fWithManyParmsAsTuple(1, 2, 3))

    type ArgType[S <: String] <: Tuple = S match
      case "" => EmptyTuple
      case _ =>
        CharAt[S, 0] match
          case '%' =>
            CharAt[S, 1] match
              case 'd' => Int *: ArgType[Substring[S, 2, Length[S]]]
              case 's' => String *: ArgType[Substring[S, 2, Length[S]]]
          case _ => ArgType[Substring[S, 1, Length[S]]]

    summon[ArgType["%s %d"] =:= Tuple2[String, Int]]
    def printfModeltype[T](s: String)(x: ArgType[s.type]): Unit = printf(s, x)
//    printfModeltype("%s %d")("name", 77)

    class StringConverter[T](f: (p: String) => T):
      val convert = f
    end StringConverter

    val intConverter = StringConverter(p => p.toInt)
    type IntValue = intConverter.type
    val strConverter = StringConverter(p => p.toString)
    type StringValue = strConverter.type

    case class Aggregator(pt: Tuple)(f: SpecialAggregatorTypes[pt.type] => Unit):
      val publicF = f

    type SpecialAggregatorTypes[T <: Tuple] <: Tuple = T match
      case EmptyTuple => EmptyTuple
      case h *: t =>
        h match
          case String             => SpecialAggregatorTypes[t]
          case Int                => SpecialAggregatorTypes[t]
          case StringConverter[v] => v *: SpecialAggregatorTypes[t]

    extension [T](x: T) infix def **:(y: Tuple): x.type *: y.type = x *: y

    val parm = "a" **: intConverter **: "b" **: strConverter **: EmptyTuple
    val agg = Aggregator("a" **: intConverter **: 37 **: strConverter **: EmptyTuple)((p1, p2) =>
      println(p1.toString + ", " + p2.toString)
    )

    println(
       "File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Language/TypeLevelPrg.scala created at time 9:46AM"
    )
