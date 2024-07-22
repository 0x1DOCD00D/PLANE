/*
 * Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package DSLWorkshop

object ColonsGame:
  def key1(value: Int): Int =
    println(s"key1: $value")
    value
  def key2(in: Int): Int =
    println(s"key2: ${in + 10}")
    in + 10

  def key3(in1: Int)(in2: Int): Int =
    println(s"key3: ${in1 + in2}")
    in1 + in2

  @main def runColonsGame(args: String*): Unit =
    key1:
      20

    key2:
      key1:
        30

    key3:
      key1:
        40
      key2:
        50

    println(
       "File /Users/drmark/IdeaProjects/PLANE/src/main/scala/DSLWorkshop/ColonsGame.scala created at time 2:38PM"
    )
