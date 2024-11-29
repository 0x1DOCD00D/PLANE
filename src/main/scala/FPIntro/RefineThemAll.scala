////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package FPIntro

object RefineThemAll:
//https://msitko.pl/blog/build-your-own-refinement-types-in-scala3.html
  import scala.compiletime.ops.int.*
  import scala.compiletime.ops.boolean.*
  import scala.compiletime.*

  trait Validated[PredRes <: Boolean]

  given Validated[true] = new Validated[true] {}

  trait RefinedInt[Predicate[_ <: Int] <: Boolean]

  def validate[V <: Int, Predicate[_ <: Int] <: Boolean](using Validated[Predicate[V]]): RefinedInt[Predicate] = new RefinedInt {}

  type LowerThan10[V <: Int] = V < 10
  val lowerThan10T: RefinedInt[LowerThan10] = validate[4, LowerThan10]

  // Equivalent with type lambdas:
  val lowerThan10: RefinedInt[[V <: Int] =>> V < 10] = validate[4, [V <: Int] =>> V < 10]

  // We can write more complex predicates:
  validate[7, [V <: Int] =>> V > 5 && V < 10]

  // The following will not compile:
  // validate[4, [V <: Int] =>> V < 10 && V > 6](4)
  // no implicit argument of type iteration1.Validated[(false : Boolean)] was found for parameter x$2 of method validate in package iteration1
  // L25:   validate[4, [V <: Int] =>> V < 10 && V > 6](4)

  sealed trait Pred

  class And[A <: Pred, B <: Pred] extends Pred

  class Leaf extends Pred

  class LowerThan[T <: Int & Singleton] extends Leaf

  class GreaterThan[T <: Int & Singleton] extends Leaf

  trait ValidatedPred[E <: Pred]

  implicit inline def mkValidated[V <: Int & Singleton, E <: Pred](v: V): ValidatedPred[E] =
    inline erasedValue[E] match
      case _: LowerThan[t] =>
        inline if constValue[V] < constValue[t]
        then new ValidatedPred[E] {}
        else
          inline val vs = constValue[compiletime.ops.any.ToString[V]]
          inline val limit = constValue[compiletime.ops.any.ToString[t]]
          error("Validation failed: " + vs + " < " + limit)
      case _: GreaterThan[t] =>
        inline if constValue[V] > constValue[t]
        then new ValidatedPred[E] {}
        else
          inline val vs = constValue[compiletime.ops.any.ToString[V]]
          inline val limit = constValue[compiletime.ops.any.ToString[t]]
          error("Validation failed: " + vs + " > " + limit)
      case _: And[a, b] =>
        inline mkValidated[V, a](v) match
          case _: Validated[_] =>
            inline mkValidated[V, b](v) match
              case _: Validated[_] => new ValidatedPred[E] {}

  def main(args: Array[String]): Unit = {
    val a: ValidatedPred[LowerThan[10]] = 6

//    val x: Validated[LowerThan[10]] = 16 // Validation fails with:
    // Validation failed: 16 < 10

//    val b: ValidatedPred[GreaterThan[5] And LowerThan[10]] = 6
    // val y: Validated[GreaterThan[5] And LowerThan[10]] = 1 // Validation fails with:
    // Validation failed: 1 > 5
    // val z: Validated[GreaterThan[5] And LowerThan[10]] = 16 // Validation fails with:
    // Validation failed: 16 < 10
  }
