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

import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class BinaryNumbersTest extends AnyFreeSpec {
  "BinaryNumbers.nlz" - {
    "count the number of bits for Int" in {
      BinaryNumbers.nlz(0x3FFAFFFF) shouldEqual 2
      BinaryNumbers.nlz(0x0FFFFFFF) shouldEqual 4
      BinaryNumbers.nlz(((1L << 32) - 1).toInt) shouldEqual 0
      BinaryNumbers.nlz(1) shouldEqual 31
      BinaryNumbers.nlz(0) shouldEqual 32
    }

    "count the number of bits for Long" in {
      BinaryNumbers.nlz(0x3FFAFFFFL) shouldEqual 34
      BinaryNumbers.nlz(0x0FFFFFFFL) shouldEqual 36
      BinaryNumbers.nlz(((1L << 32) - 1).toLong) shouldEqual 32
      BinaryNumbers.nlz(1L) shouldEqual 63
      BinaryNumbers.nlz(0L) shouldEqual 64
      BinaryNumbers.nlz(0xFFFFFFFFFFFFFFFFL) shouldEqual 0
      BinaryNumbers.nlz(0x7FFFFFFFFFFFFFFFL) shouldEqual 1
      BinaryNumbers.nlz(0x3FFFFFFFFFFFFFFFL) shouldEqual 2
      BinaryNumbers.nlz(0x2FFFFFFFFFFFFFFFL) shouldEqual 2
      BinaryNumbers.nlz(0x2FAFAFFAFAFFAFAFL) shouldEqual 2
    }
  }

  "BinaryNumbers.width" - {
    "return the number of bits after the first nonzero bit" in {
      BinaryNumbers.width(1) shouldEqual 1
      BinaryNumbers.width(2) shouldEqual 2
      BinaryNumbers.width(3) shouldEqual 2
      BinaryNumbers.width(0xFA) shouldEqual 8
      BinaryNumbers.width(0xFAFAFAFA) shouldEqual 32
    }
  }

}
