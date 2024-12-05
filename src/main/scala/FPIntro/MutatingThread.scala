////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package FPIntro

object MutatingThread:

  import java.util.concurrent.{Executors, ThreadFactory}

  @main def runMutatingThread(args: String*): Unit = {
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/FPIntro/MutatingThread.scala created at time 11:09AM")
    val threadFactory = new ThreadFactory {
      private val counter = new java.util.concurrent.atomic.AtomicInteger(0)

      def newThread(r: Runnable): Thread = {
        val thread = new Thread(r)
        thread.setName(s"Worker-${counter.incrementAndGet()}")
        thread
      }
    }

    val executor = Executors.newFixedThreadPool(5, threadFactory)

    var sharedVar = 0

    // Lambda function that mutates the shared variable
    val mutateFunction: Runnable = () => {
//      this.synchronized {
      val currentValue = sharedVar
      sharedVar = currentValue + 1
      println(s"Thread ${Thread.currentThread().getName}: sharedVar = $sharedVar")
//      }
    }

    for (_ <- 1 to 50) {
      executor.submit(mutateFunction)
    }

    executor.shutdown()
  }
