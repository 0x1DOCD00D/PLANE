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

object OtherNatExample {

  sealed trait Nat {
    type plus[Other <: Nat] <: Nat
  }

  sealed trait ZeroNat extends Nat {
    type plus[Other <: Nat] = Other
  }

  sealed trait NotZeroNat[Prev <: Nat] extends Nat {
    type plus[Other <: Nat] = NotZeroNat[Prev#plus[Other]]
  }

  type +[Term1 <: Nat, Term2 <: Nat] = Term1#plus[Term2]

  type Nat1 = NotZeroNat[ZeroNat]
  type Nat2 = NotZeroNat[Nat1]
  type Nat3 = Nat1 + Nat2
  type Nat4 = +[Nat3, Nat1]
  type Nat4_alter = Nat2 + Nat2
  implicitly[Nat4 =:= Nat4_alter]

  sealed trait TypedList {
    type Size <: Nat
    type map[F[Nat] <: Nat] <: TypedList
  }

  sealed trait NilTypedList extends TypedList {
    type Size = ZeroNat
    type map[F[Nat] <: Nat] = NilTypedList
  }

  sealed trait ::[Value <: Nat, OtherList <: TypedList] extends TypedList {
    override type Size = NotZeroNat[OtherList#Size]
    type map[F[Nat] <: Nat] = F[Value] :: OtherList#map[F]
  }

  /*
    type NatList1 = Nat1 :: Nat2 :: Nat3 :: Nat4

    type NatList2 = ::[Nat1, ::[Nat2, ::[Nat3, Nat4]]]
  */

  //  implicitly[NatList1,NatList2]
}
