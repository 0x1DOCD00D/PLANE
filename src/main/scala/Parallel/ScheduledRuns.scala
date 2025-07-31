
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Parallel

object ScheduledRuns {
  import java.util.concurrent.{CountDownLatch, Executors, TimeUnit}

  def main(args: Array[String]): Unit = {
    val scheduler = Executors.newSingleThreadScheduledExecutor()
    // if you only want one “hi” before you quit, use CountDownLatch(1)
    val c         = new CountDownLatch(2)

    val r: Runnable = () => {
      println("hi")
      c.countDown()
    }

    // first run at 1 s, then every 1 s thereafter
    scheduler.scheduleAtFixedRate(r, 1L, 1L, TimeUnit.SECONDS)

    // block here until we’ve seen two “hi”s
    c.await()

    // clean up the executor and then exit
    scheduler.shutdown()
    println("Done.")
  }
}
