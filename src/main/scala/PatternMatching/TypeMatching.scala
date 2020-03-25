/*
 * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package PatternMatching

import PatternMatching.TypeMatching.Subtype11

object TypeMatching {

  trait SuperType

  trait Subtype1 extends SuperType

  trait Subtype2 extends SuperType

  trait Subtype11 extends Subtype1

  trait Subtype21 extends Subtype2

  def apply(arg: SuperType): SuperType = arg match {
    case x: SuperType => println("SuperType"); x
    case x: Subtype1 => println("subtype1"); x
    case x: Subtype2 => println("subtype2"); x
    case x: Subtype11 => println("subtype11"); x
    case x: Subtype21 => println("subtype21"); x
  }

  def apply1(arg: SuperType): SuperType = arg match {
    case x: Subtype11 => println("subtype11"); x
    case x: Subtype1 => println("subtype1"); x
    case x: Subtype21 => println("subtype21"); x
    case x: Subtype2 => println("subtype2"); x
    case x: SuperType => println("SuperType"); x
  }

}

object RunIt extends App {
  TypeMatching.apply1(new Subtype11 {})
}