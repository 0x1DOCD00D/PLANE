
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Parallel

import java.util.concurrent.TimeUnit
import scala.collection.parallel.ForkJoinTaskSupport
import java.util.concurrent.ForkJoinPool
import scala.collection.parallel.ForkJoinTaskSupport
import scala.collection.parallel.CollectionConverters.*
import scala.collection.parallel.ParSeq

object ParallelBehavior extends App {

  val CHANGE_ME = false

  val externalParallelism = if (CHANGE_ME) 10 else 2

  val l = (1 until 100).par
  val l2 = List(1).par // par but with only one element

  l2.tasksupport = new ForkJoinTaskSupport(
    new ForkJoinPool(externalParallelism))
  l.tasksupport = new ForkJoinTaskSupport(new ForkJoinPool(2))

  l2.map { j =>
    l.map { i =>
      println(s"STARTED $i")
      TimeUnit.SECONDS.sleep(10000) // blocking
      println(s"DONE $i")
    }
  }
}