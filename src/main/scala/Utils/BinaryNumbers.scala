/*
 *
 *  * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package Utils

object BinaryNumbers {
  def unsignedLess(x: Long, y: Long): Boolean = (x < y) ^ ((x < 0) != (y < 0))

  def unsignedLess(x: Int, y: Int): Boolean = (x < y) ^ ((x < 0) != (y < 0))

  def unsignedLessEq(x: Long, y: Long): Boolean = ((x < y) ^ ((x < 0) != (y < 0))) | (x == y)

  def unsignedLessEq(x: Int, y: Int): Boolean = ((x < y) ^ ((x < 0) != (y < 0))) | (x == y)

  /** count number of leading zeros. Uses binary search */
  def nlz(value: Int): Int = {
    var x = value
    if (x == 0)
      return 32
    var n = 0
    if (unsignedLessEq(x, 0x0000FFFF)) {
      n += 16
      x <<= 16
    }
    if (unsignedLessEq(x, 0x00FFFFFF)) {
      n += 8
      x <<= 8
    }
    if (unsignedLessEq(x, 0x0FFFFFFF)) {
      n += 4
      x <<= 4
    }
    if (unsignedLessEq(x, 0x3FFFFFFF)) {
      n += 2
      x <<= 2
    }
    if (unsignedLessEq(x, 0x7FFFFFFF))
      n += 1
    n
  }

  /** count number of leading zeros */
  def nlz(value: Long): Int = {
    var x = value
    if (x == 0)
      return 64
    var n = 0

    if (unsignedLessEq(x, 0x00000000FFFFFFFFL)) {
      n += 32
      x <<= 32
    }
    if (unsignedLessEq(x, 0x0000FFFFFFFFFFFFL)) {
      n += 16
      x <<= 16
    }
    if (unsignedLessEq(x, 0x00FFFFFFFFFFFFFFL)) {
      n += 8
      x <<= 8
    }
    if (unsignedLessEq(x, 0x0FFFFFFFFFFFFFFFL)) {
      n += 4
      x <<= 4
    }
    if (unsignedLessEq(x, 0x3FFFFFFFFFFFFFFFL)) {
      n += 2
      x <<= 2
    }
    if (unsignedLessEq(x, 0x7FFFFFFFFFFFFFFFL))
      n += 1
    n
  }

  def width(x: Int): Int = 32 - nlz(x)
}
