
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scala3Features

object PathDependent:
  val vInt: Int = ???
  val vvInt: vInt.type = vInt // 2 is rejected
  /*
    Found:    (2 : Int)
    Required: (scala3Features.PathDependent.vInt : Int)
   * */

  object NestedOne:
    val v = 1
    val vv: v.type = v

//  summon[NestedOne.v.type =:= 1]
  summon[NestedOne.v.type =:= NestedOne.vv.type ]

  abstract class Box {
    type T
    var value: T
  }

  class BoxManager {
    def createIntBox(): Box { type T = Int } = new Box {
      type T = Int
      var value: T = 0
    }

    def createStringBox(): Box { type T = String } = new Box {
      type T = String
      var value: T = ""
    }
  }

  val manager = new BoxManager

  val intBox: Box { type T = Int } = manager.createIntBox()
  val stringBox: Box { type T = String } = manager.createStringBox()

  intBox.value = 42 // OK: intBox.value is of type Int
  stringBox.value = "Hi" // OK: stringBox.value is of type String

  def main(args: Array[String]): Unit = {
    val x: intBox.T = 20
//    val y: intBox.T = ""
    println(x)
  }
