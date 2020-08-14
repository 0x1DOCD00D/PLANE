/*
 *
 *  * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package TypeFP

object ClassicNatExample {

  //we introduce a type called Nat to model the concept of natural numbers.
  //by definition, the standard ISO 80000-2,] begin the natural numbers with 0, corresponding to the non-negative
  // integers 0, 1, 2, 3, ... (collectively denoted by the symbol {\displaystyle \mathbb {N} _{0}}\mathbb {N} _{0}),
  // whereas others start with 1, corresponding to the positive integers 1, 2, 3, ...
  // (collectively denoted by the symbol {\displaystyle \mathbb {N} _{1}}\mathbb {N} _{1}).
  //An integer (from the Latin integer meaning "whole")[a] is colloquially defined as a number that can be
  // written without a fractional component.

  trait Nat

  /*  The Peano axioms define the arithmetical properties of natural numbers, usually represented as a set N or {\displaystyle \mathbb {N} .}\mathbb {N} . The non-logical symbols for the axioms consist of a constant symbol 0 and a unary function symbol S.

    The first axiom states that the constant 0 is a natural number:

      0 is a natural number.
      The next four axioms describe the equality relation. Since they are logically valid in first-order logic with equality, they are not considered to be part of "the Peano axioms" in modern treatments.[5]

    For every natural number x, x = x. That is, equality is reflexive.
      For all natural numbers x and y, if x = y, then y = x. That is, equality is symmetric.
      For all natural numbers x, y and z, if x = y and y = z, then x = z. That is, equality is transitive.
      For all a and b, if b is a natural number and a = b, then a is also a natural number. That is, the natural numbers are closed under equality.
      The remaining axioms define the arithmetical properties of the natural numbers. The naturals are assumed to be closed under a single-valued "successor" function S.

    For every natural number n, S(n) is a natural number. That is, the natural numbers are closed under S.
      For all natural numbers m and n, m = n if and only if S(m) = S(n). That is, S is an injection.
    For every natural number n, S(n) = 0 is false. That is, there is no natural number whose successor is 0.*/

  class `0` extends Nat

  //  class `1` extends Nat
  //  class `2` extends Nat
  //we cannot just define all nats here by enumeration

  class SuccessorOf[Number <: Nat] extends Nat

  type `1` = SuccessorOf[`0`]
  type `2` = SuccessorOf[`1`]
  type alt_2 = SuccessorOf[SuccessorOf[`0`]]
  type `3` = SuccessorOf[`2`]
  type `4` = SuccessorOf[`3`]
  type `5` = SuccessorOf[`4`]
  //and this is a clumsy type specification if we do not use aliases
  type alt_5 = SuccessorOf[SuccessorOf[SuccessorOf[SuccessorOf[SuccessorOf[`0`]]]]]

  //at this point we want to define some operations on these numbers. One operation is called comparison and it is
  //important for establishing the ordering relation between natural numbers.
  trait <[NumberLeft <: Nat, NumberRight <: Nat]

  //Instantiating a type proves that there exists a member of this type for which its constraints hold
  //if the compiler can create an instance for a given type, then the proof of the correct
  //prefix notation
  val compare_0_with_5: <[`0`, `5`] = implicitly[<[`0`, `5`]]
  //infix notation
  val compare_0_with_3: `0` < `3` = implicitly[<[`0`, `3`]]


  //to make a compiler create an instance of a given type automatically, we introduce some scaffolding using implicits
  //the first hypothesis is that the successor of every nat number is greater than zero `0`
  implicit def alwaysGreaterThanZero[GtThanZero <: Nat]: <[`0`, SuccessorOf[GtThanZero]] = new <[`0`, SuccessorOf[GtThanZero]] {}

  //works fine when we compare 0 and 5 or 0 and 2,
  //but it fails here because no implicit object is found for 4 and 5
  val compare_0_with_2: <[`0`, `2`] = implicitly[<[`0`, `2`]]

  //this is an inductive definition employed by the implicit feature of scala. First, it checks the object of the type <[`4`,`5`], which can be produced
  //from the implicit object of predecessors whose successors are `4` and `5`. This implicit object is lessThen: <[LeftNumber, RightNumber] where LeftNumber is `3` and RightNumber is `4` and
  //this object can in turn be produced by this implicit inductionStep as the <[SuccessorOf[`2`], SuccessorOf[`3`]] where  lessThen: <[LeftNumber, RightNumber] where LeftNumber is `1` and RightNumber is `2`.
  //Continuing this logic we have the resulting object <[`0`, `1`] and we are done!
  implicit def inductionStep[LeftNumber <: Nat, RightNumber <: Nat](implicit lessThen: <[LeftNumber, RightNumber]): <[SuccessorOf[LeftNumber], SuccessorOf[RightNumber]] = new <[SuccessorOf[LeftNumber], SuccessorOf[RightNumber]] {}

  val compare_4_with_5: <[`4`, `5`] = implicitly[<[`4`, `5`]]
}
