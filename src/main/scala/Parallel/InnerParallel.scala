
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Parallel

//https://github.com/scala/scala-parallel-collections/issues/283

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.ForkJoinPool
import scala.collection.parallel.CollectionConverters.*
import scala.collection.parallel.ForkJoinTaskSupport

object InnerPar {
  val numTasks = 100
  val numThreads = 10

  /** return maximum number of simultaneous threads running when requesting numThreads */
  def forkJoinPoolMaxThreads(useInnerPar:Boolean): Int = {

    // every thread in the outer collection will increment
    // and decrement this counter as it starts and exits
    val threadCounter = new AtomicInteger(0)

    // this function increments and decrements threadCounter
    // on start and exit, optionally creates an inner parallel collection
    // and finally returns the thread count it found at startup
    def incrementAndCountThreads(idx:Int):Int = {
      val otherThreadsRunning:Int = threadCounter.getAndAdd(1)
      if (useInnerPar) {
        (0 until 20).par.map { elem => elem + 1 }
      }
      Thread.sleep(10)
      threadCounter.getAndAdd(-1)
      otherThreadsRunning + 1
    }

    // create parallel collection using a ForkJoinPool with numThreads
    val parCollection = (0 until numTasks).toVector.par
    parCollection.tasksupport = new ForkJoinTaskSupport(new ForkJoinPool(numThreads))
    val threadCountLogList = parCollection.map { idx =>
      incrementAndCountThreads(idx)
    }

    // return the maximum number of simultaneous threads
    // running simultaneously (as counted on each thread start)
    threadCountLogList.max
  }


  def main(args:Array[String]):Unit = {
    val testConfigs = Seq(true, false, true, false)
    testConfigs.foreach { useInnerPar =>
      val maxThreads = forkJoinPoolMaxThreads(useInnerPar)

      // the total number of threads running should not have exceeded
      // numThreads at any point
      val isSuccess = maxThreads <= numThreads

      println(f"useInnerPar $useInnerPar%6s, success is $isSuccess%6s   (maxThreads $maxThreads%4d)")
    }
  }
}