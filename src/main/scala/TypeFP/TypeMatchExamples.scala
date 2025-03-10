
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package TypeFP

import scala.compiletime.ops.int.+
import scala.concurrent.Future

object TypeMatchExamples:
  type DepthOf[T] <: Int = T match
    case List[t] => 1 + DepthOf[t]
    case _       => 0

  type Append[L1 <: Tuple, L2 <: Tuple] <: Tuple = L1 match
    case EmptyTuple   => L2
    case head *: tail => head *: Append[tail, L2]

  type ContainsType[T <: Tuple, X] <: Boolean = T match
    case EmptyTuple => false
    case X *: _     => true // If the head matches X, return true
    case _ *: tail  => ContainsType[tail, X] // Recursively check the tail
    case List[t]    => ContainsType[t, X] // If it's a List, recurse into its type
    case _          => false // Base case: not found

  type L1 = (Int, String)
  type L2 = (Boolean, Double)
  type AppendedList = Append[L1, L2] // (Int, String, Boolean, Double)

  type ConvertIntToString[T] = T match
    case Int       => String
    case List[t]   => List[ConvertIntToString[t]]
    case Option[t] => Option[ConvertIntToString[t]]
    case _         => T

  import scala.Tuple.Concat
  type Flatten[T <: Tuple] <: Tuple = T match
    case EmptyTuple => EmptyTuple
    case h *: t => h match
        case Tuple => Concat[Flatten[h], Flatten[t]]
        case _     => h *: Flatten[t]

  def main(args: Array[String]): Unit = {
    val depth0: DepthOf[Int] = 0 // No nesting
    val depth1: DepthOf[List[Int]] = 1 // One level of nesting
    val depth2: DepthOf[List[List[Int]]] = 2 // Two levels of nesting
    val depth3: DepthOf[List[List[List[Int]]]] = 3 // Three levels of nesting

    implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

    val ff: Future[Append[L1, L2]] = Future {
      (1, "a", true, 3.7f)
    }
    val tpl: Append[L1, L2] = (1, "a", true, 3.7f)

    summon[ContainsType[(1, String, 3, Double), 3] =:= true]
//    summon[ContainsType[(1, String, 3, Double), 5] =:= true]

    type Converted1 = ConvertIntToString[Int] // String
    type Converted2 = ConvertIntToString[Option[Int]] // Option[String]
    type Converted3 = ConvertIntToString[List[Int]] // List[String]
    type Converted4 = ConvertIntToString[Double] // Double

    type Flattened1 = Flatten[(Int, (String, (Boolean, Double)))] // (Int, String, Boolean, Double)
    val fltnd: Flattened1 = (1, "a", true, 7.2d)

  }
