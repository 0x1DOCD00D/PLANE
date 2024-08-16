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

object ByteArrayConversions extends App {
  val byteArray: Array[Byte] = Array(0x12, 49, 57, 54, 54, 0x1c, 0x5, 86, 35, 81, 0xc)
  val byte2Integer: Array[Byte] = Array(0, 0, 0, 0)
  Array.copy(byteArray, 1, byte2Integer, 0, 4)
  val source = 1966

  val ba = source.toString.toCharArray
  println(ba)
  val result = ba.toString.toInt
  println(ba)
}
