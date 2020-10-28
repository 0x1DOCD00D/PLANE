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

import java.security.MessageDigest

import org.apache.commons.codec.binary.Base64

object MD5 {
  def hexDigest(x: Array[Byte]): String = {
    MessageDigest.getInstance("MD5").digest(x).map("%02x" format _).mkString
  }

  def base64Digest(x: Array[Byte]): String = {
    new Base64(true).encodeAsString(MessageDigest.getInstance("MD5").digest(x)).stripLineEnd
  }
}

object SHA256 {
  def hexDigest(x: Array[Byte]): String = {
    MessageDigest.getInstance("SHA-256").digest(x).map("%02x" format _).mkString
  }

  def base64Digest(x: Array[Byte]): String = {
    new Base64(true).encodeAsString(MessageDigest.getInstance("SHA-256").digest(x)).stripLineEnd
  }
}
