////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package DSLWorkshop

import DSLWorkshop.ColonsGame.key1

import scala.annotation.experimental

@experimental
object ColonsGame:
  def key(v1: => Int, v2: => Int): Int =
    println(s"key: $v1, $v2")
    v1 + v2

  val ZZ = X

  def X[T](value: => T): T =
    println(s"key: $value")
    value

  def Y[T](value: => T): T =
    println(s"key: $value")
    value

  def Z[T](value: => T): T =
    println(s"key: $value")
    value

  def key1(value: => Int): Int =
    println(s"key1: $value")
    value

  def key3(value: => Int): Int =
    println(s"key3: $value")
    value

  def key2(in: => Int): Int =
    println(s"key2: ${in + 10}")
    in + 10

  def key31(in1: => Int)(in2: => Int): Int =
    println(s"key3: ${in1 + in2}")
    in1 + in2

  @main def runColonsGame(args: String*): Unit =
    val getIt =
      key(
         10,
         20
      )

    val result = key3:
      key1:
        40
      key2:
        key2:
          2
        key3:
          key1:
            20

    val resultX = X:
      Y:
        "val1"
      Y:
        X:
          2.0f
        Z:
          ZZ:
            20L
    println(
       "File /Users/drmark/IdeaProjects/PLANE/src/main/scala/DSLWorkshop/ColonsGame.scala created at time 2:38PM"
    )
    println(resultX)
