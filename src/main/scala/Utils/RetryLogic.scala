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

object RetryLogic {
  /**
   * Wrap a block that might throw an exception and retry the block for the specified number of times (@param n)
   * when the retries are over the exception is propagated
   *
   * @param n        Number of retries
   * @param block    Block to execute
   * @param delay_ms delay in miliseconds between retries (Uses Thread.sleep)
   * @param base     base of the exponential backoff
   *                 0 will disable waiting,
   *                 1 will wait for delay_ms for all the retries,
   *                 2 will wait for delay_ms, delay_ms * 2, * delay_ms * 4 etc.
   */
  def apply[T](n: Int)(block: => T, delay_ms: Long = 1000, base: Long = 2): T = {
    try {
      block
    } catch {
      case e if n > 1 => {
        Thread.sleep(delay_ms)
        RetryLogic(n - 1)(block, delay_ms * base, base)
      }
    }
  }
}
