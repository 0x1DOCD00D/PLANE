
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package TypeFP

//https://users.scala-lang.org/t/class-dependent-type-with-implicits-2-13-16/10832/4

import scala.language.implicitConversions
import scala.math.Numeric
import scala.Conversion

object DependentTypeConv {

  trait Block[T] {
    type Result
    def apply(bi: T): Result
  }

  type Aux[T, R] = Block[T] { type Result = R }

  object Block {

    def conv[T, R](f: T => R): Aux[T, R] =
      new Block[T] {
        type Result = R
        def apply(value: T): Result = f(value)
      }

    given functionToBlock[T, R]: Conversion[T => R, Aux[T, R]] with
      def apply(f: T => R): Aux[T, R] = conv(f)
  }

  import Block.conv
  import Block.given

  def doSth[T](using Numeric[T]) = new PartiallyAppliedDoSth[T]

  class PartiallyAppliedDoSth[T](using n: Numeric[T]) {
    def apply[R](block: Aux[T, R]): R = block(n.zero)
  }

  def main(args: Array[String]): Unit = {
    val good: Int = doSth[Int](conv(_ + 5))                // explicit helper
    val bad : Int = doSth[Int](_ + 5)                      // uses the given Conversion
    println(good)                                          // 5
    println(bad)                                           // 5
  }
}
