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

object CompileIfTypesAreDifferent {

  //  sealed abstract class =:=[From, To] extends (From <:< To) with Serializable {
  //  implicit def refl[A]: A =:= A = singleton.asInstanceOf[A =:= A]
  //private val singleton: =:=[Any, Any] = new =:=[Any,Any] {
  //  with this declaration, it is possible to compile an instance of this case class
  //  only if both arguments are of the same type, otherwise the compiler issues an error
  case class SomeClassTypeParams[A, B](a: A, b: B)(implicit evidence: A =:= B)

  trait X

  trait Xext extends X

  //  both parameters are of type int
  val ok2Compile1 = SomeClassTypeParams(10, 20)

  //  the types are different or one is a subtype of the other
  //  then the compiler issues an error
  //  val ok2Compile2 = SomeClassTypeParams(new X {}, new Xext {})

  //  we can fix this problem with a different evidence that demands that B is a subtype of A
  case class SomeClassTypeParamsExtend[A, B](a: A, b: B)(implicit evidence: B <:< A)

  val ok2Compile3 = SomeClassTypeParamsExtend(new X {}, new Xext {})

  //  let us introduce a different constraint. we want the compiler to accept the code
  //  only if the types of the case class below are different, A != B, (implicit evidence: A =!= B)
  case class DifferentClassTypeParams[A, B](a: A, b: B)

  //first step: we create a new type, =!=
  //it should create an evidence object is A and B are different and fail if they are the same
  trait =!=[A, B]

  type IntNotEqualToString = Int =!= String

  //  implicit val evidenceNot = new =!=[Int, String]{}
  //  how many implicits like this do we need?! Argh!
  //  the solution is to exploit the property of implicit object search where only
  //  one implicit object should be found in the given scope to resolve the reference to it
  implicit def neqMeth[A, B]: =!=[A, B] = null

  implicit def neqMeth1[A]: =!=[A, A] = null

  implicit def neqMeth2[A]: =!=[A, A] = null

  case class NotEqualClassTypeParams[A, B](a: A, b: B)(implicit evidence: A =!= B)

  val ok2Compile4 = NotEqualClassTypeParams(new X {}, new Xext {})
  val ok2Compile5 = NotEqualClassTypeParams(25, "DrMark")
  //this expression fails because the implicit cannot be resolved since both implicit
  //evidence methods, neqMeth1 and neqMeth2 are triggered
  //  val ok2Compile6 = NotEqualClassTypeParams(30,50)
}
