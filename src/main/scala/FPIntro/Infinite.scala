/*
 * Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package FPIntro

object Infinite:
  def loop1(i: Int): Int =
    if i == 2 then loop2(1)
    else loop2(i + 1)

  def loop2(i: Int): Int =
    if i == 1 then loop1(2)
    else loop1(i + 1)

  def f(i: Int): Unit =
    if i == 2 then println("Done  2")
    else println("Done  1")
  def main(args: Array[String]): Unit =
    f(loop1(2))