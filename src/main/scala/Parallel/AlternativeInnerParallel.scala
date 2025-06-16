////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Parallel

package nestedpar

//https://github.com/scala/scala-parallel-collections/issues/283

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.ForkJoinPool
import scala.collection.parallel.ForkJoinTaskSupport
import scala.collection.parallel.CollectionConverters.*
import scala.collection.parallel.ParSeq

object AlternativeInnerParallel {
  val numTasks = 10
  val numThreads = 4

  /** return maximum number of simultaneous threads running when requesting numThreads */
  def forkJoinPoolMaxThreads(useInnerPar: Boolean): Int = {
    val pool = new ForkJoinPool(
       numThreads
       /*
      ForkJoinPool.defaultForkJoinWorkerThreadFactory,
      null,
      true,
      4,
      4,
      1,
      null,
      60,
      java.util.concurrent.TimeUnit.SECONDS
        */
    )
    /* Java 9 API
    ForkJoinPool(int parallelism, ForkJoinPool.ForkJoinWorkerThreadFactory factory, Thread.UncaughtExceptionHandler handler, boolean asyncMode, int corePoolSize, int maximumPoolSize, int minimumRunnable, Predicate<? super ForkJoinPool> saturate, long keepAliveTime, TimeUnit unit)
     */
    val taskSupport = new ForkJoinTaskSupport(pool)

    // every thread in the outer collection will increment
    // and decrement this counter as it starts and exits
    val threadCounter = new AtomicInteger(0)

    // this function increments and decrements threadCounter
    // on start and exit, optionally creates an inner parallel collection
    // and finally returns the thread count it found at startup
    def incrementAndCountThreads(idx: Int): Int = {
      println(s"Outer op on: ${Thread.currentThread}")
      val threadsRunning: Int = threadCounter.incrementAndGet()
      if (useInnerPar) {
        val c = (0 until 20).par
        c.tasksupport = taskSupport
        println(s"Inner op on: ${Thread.currentThread}, c.tasksupport is ${c.tasksupport}, env is ${c.tasksupport.environment}")
        c.map { elem =>
          println(s"Inner map on: ${Thread.currentThread}")
          elem + 1
        }
        // (0 until 20).toSeq.par.map { elem => elem + 1 }
      } else {
        /* Any execute on a different pool shows the symptom
        List(1).par.map { elem =>
          println(s"Inner map on: ${Thread.currentThread}")
          elem + 1
        }
         */
      }
      Thread.sleep(10L)
      threadCounter.decrementAndGet()
      println(s"Running: $threadsRunning")
      threadsRunning
    }
    // create parallel collection using a ForkJoinPool with numThreads
    val parCollection = (1 to numTasks).toVector.par
    // parCollection.tasksupport = new ForkJoinTaskSupport(new ForkJoinPool(numThreads))
    parCollection.tasksupport = taskSupport
    println(s"parC.tasksupport is ${parCollection.tasksupport}, env is ${parCollection.tasksupport.environment}")
    val threadCountLogList = parCollection.map { idx =>
      incrementAndCountThreads(idx)
    }
    // return the maximum number of simultaneous threads
    // running simultaneously (as counted on each thread start)
    threadCountLogList.max
  }

  def main(args: Array[String]): Unit = {
    println(s"global is ${concurrent.ExecutionContext.global}")
    // val testConfigs = Seq(true, false, true, false)
    val testConfigs = Seq(true)
    testConfigs.foreach { useInnerPar =>
      val maxThreads = forkJoinPoolMaxThreads(useInnerPar)

      // the total number of threads running should not have exceeded
      // numThreads at any point
      val isSuccess = maxThreads <= numThreads

      println(f"useInnerPar $useInnerPar%6s, success is $isSuccess%6s   (maxThreads $maxThreads%4d)")
    }
  }
}
