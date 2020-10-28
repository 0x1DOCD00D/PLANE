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

import java.nio.ByteBuffer
import java.nio.ByteOrder.LITTLE_ENDIAN

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

import scala.collection.mutable.ArrayBuffer

class IntegerStoreRetrieveTest extends AnyFreeSpec {
  "IntegerStoreRetrieve" - {
    "encode and decode SignedInt" - {
      val xs = List(0, 1, 4, -1, 0x7fffffff, -2, -5)
      val buff = new ArrayBuffer[Byte]()
      for (x <- xs) {
        IntegerStoreRetrieve.writeSignedInt(x, buff)
      }
      val bbuff = ByteBuffer.wrap(buff.toArray).order(LITTLE_ENDIAN)
      for (x <- xs) {
        val r = IntegerStoreRetrieve.readSignedInt(bbuff)
        r shouldEqual x
      }
    }

    "encode and decode SignedLong" - {
      val xs = List[Long](0, 1, 4, -1, 0x7fffffff, -2, -5, 0x7fffffffffffffffL)
      val buff = new ArrayBuffer[Byte]()
      for (x <- xs) {
        IntegerStoreRetrieve.writeSignedLong(x, buff)
      }
      val bbuff = ByteBuffer.wrap(buff.toArray).order(LITTLE_ENDIAN)
      for (x <- xs) {
        val r = IntegerStoreRetrieve.readSignedLong(bbuff)
        r shouldEqual x
      }
    }

    "encode and decode UnsignedInt" in {
      val xs = List(0, 1, 4, -1, 0x7fffffff, -2, -5)
      val buff = new ArrayBuffer[Byte]()
      for (x <- xs) {
        IntegerStoreRetrieve.writeUnsignedInt(x, buff)
      }
      val bbuff = ByteBuffer.wrap(buff.toArray).order(LITTLE_ENDIAN)
      for (x <- xs) {
        val r = IntegerStoreRetrieve.readUnsignedInt(bbuff)
        r shouldEqual x
      }
    }

    "encode and decode UnsignedLong" in {
      val xs = List[Long](0, 1, 4, -1, 0x7fffffff, -2, -5)
      val buff = new ArrayBuffer[Byte]()
      for (x <- xs) {
        IntegerStoreRetrieve.writeUnsignedLong(x, buff)
      }
      val bbuff = ByteBuffer.wrap(buff.toArray).order(LITTLE_ENDIAN)
      for (x <- xs) {
        val r = IntegerStoreRetrieve.readUnsignedLong(bbuff)
        r shouldEqual x
      }
    }

  }

}
