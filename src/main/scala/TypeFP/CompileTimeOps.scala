////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package TypeFP

import scala.compiletime.ops.boolean.&&

object CompileTimeOps:

  import scala.compiletime.ops.int._
  import scala.compiletime.{constValue, summonInline}

  case class Point[X <: Int, Y <: Int](x: X, y: Y)

  sealed trait Shape

  case class Circle[r <: Int](radius: r) extends Shape

  // Define a Rectangle that has a width, height, and a top-left corner Point
  case class Rectangle[W <: Int, H <: Int, P <: Point[?, ?]](w: W, h: H, p: P)

  case class Triangle[b <: Int, h <: Int](base: b, height: h) extends Shape

  type XCoord[P <: Point[?, ?]] <: Int = P match {
    case Point[x, ?] => x
  }

  type YCoord[P <: Point[?, ?]] <: Int = P match {
    case Point[?, y] => y
  }

  type Area[S <: Shape] <: Int = S match {
    case Circle[r]          => r * r * 3 // Circle area (Pi ~ 3)
    case Rectangle[w, h, p] => w * h
    case Triangle[b, h]     => (b * h) / 2 // Triangle area
  }

  type IsPointWithinLimits[X <: Int, Y <: Int, LimXfrom <: Int, LimXto <: Int, LimYfrom <: Int, LimYto <: Int] = (LimXfrom <= X) && (X <= LimXto) && (LimYfrom <= Y) && (Y <= LimYto)

  type AreaOfRectangle[W <: Int, H <: Int] = W * H
  type ConditionalArea[W <: Int, H <: Int, P <: Point[?, ?]] <: Int = P match {
    case Point[x, y] =>
    IsPointWithinLimits[x, y, 1, 10, 2, 20] match {
      case true => AreaOfRectangle[W, H] * 2 // If point within limits, double the area
      case false => AreaOfRectangle[W, H] // Otherwise, calculate area normally
    }
  }

  type MyPoint = Point[2, 3]
  type MyRectangle = Rectangle[4, 5, MyPoint]

  val xCoordCheck: Boolean = constValue[XCoord[MyPoint]] == 2 // X = 2
  val yCoordCheck: Boolean = constValue[YCoord[MyPoint]] == 3 // Y = 3

  inline def checkArea[A <: Int](expected: Int): Boolean = constValue[A] == expected

  val circleAreaCheck: Boolean = checkArea[27](27) // Expected: 27
  val rectangleAreaCheck: Boolean = checkArea[20](20) // Expected: 20
  val triangleAreaCheck: Boolean = checkArea[7](7) // Expected: 7

  type ConcatTuples[T1 <: Tuple, T2 <: Tuple] <: Tuple = T1 match {
    case EmptyTuple => T2
    case h *: t     => h *: ConcatTuples[t, T2]
  }

  type LengthOf[T <: Tuple] <: Int = T match {
    case EmptyTuple => 0
    case _ *: t     => 1 + LengthOf[t]
  }

  type ConcatLength[L1 <: Tuple, L2 <: Tuple] = LengthOf[L1] + LengthOf[L2] =:= LengthOf[ConcatTuples[L1, L2]]

  // Proving the length theorem for concatenation of two tuples
  val proof: ConcatLength[(Int, String), (Boolean, Char)] = summon[
     LengthOf[(Int, String)] + LengthOf[(Boolean, Char)] =:= LengthOf[ConcatTuples[(Int, String), (Boolean, Char)]]
  ]

  @main def runCompileTimeOps(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/TypeFP/CompileTimeOps.scala created at time 10:22AM")
    println(proof)
