////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package FPIntro

object MutableEscapesScopeDeclaration:

  import java.util.concurrent.{Executors, ThreadFactory}

  @main def runMutableEscapesScopeDeclaration(args: String*): Unit = {
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/FPIntro/MutableEscapesScopeDeclaration.scala created at time 11:18AM")
    // Create a thread pool with custom thread naming
    val threadFactory = new ThreadFactory {
      private val counter = new java.util.concurrent.atomic.AtomicInteger(0)

      def newThread(r: Runnable): Thread = {
        val thread = new Thread(r)
        thread.setName(s"Worker-${counter.incrementAndGet()}")
        thread
      }
    }

    val executor = Executors.newFixedThreadPool(20, threadFactory)

    // Create a method that returns a lambda function using a mutable variable
    def createTask(): Runnable = {
      var mutableVar = 0 // Mutable variable scoped to this method
      () => {
        for (_ <- 1 to 5) {
          mutableVar += 1
          println(s"Thread ${Thread.currentThread().getName}: mutableVar = $mutableVar")
          Thread.sleep(10) // Simulate some work
        }
      }
    }

    // Call the function to get a new Runnable, and pass it to multiple threads
    val sharedTask = createTask() // `mutableVar` escapes through the returned Runnable

    // Submit the same Runnable to multiple threads
    executor.submit(sharedTask)
    executor.submit(sharedTask)

    // Shut down the executor after tasks are finished
    executor.shutdown()
  }
