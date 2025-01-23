////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package FPIntro

import scala.util.Random
import scala.util.control.Breaks.{break, breakable}

//https://users.scala-lang.org/t/functional-style-for-infinite-loop/5949/12
object InfiniteLoops:
  class Service:
    def take(): Boolean = new Random().nextInt() < 2_100_000_000

  given Service = Service()

  class NaiveTask extends Runnable {
    override def run(): Unit = {
      breakable {
        while (true) {
          val status = summon[Service].take()
          println(status)
          if (!status) break
        }
      }
    }
  }

  class JavaThreadTask extends Runnable {
    override def run(): Unit = {
      checkStatus
    }

    @scala.annotation.tailrec
    private def checkStatus(using service: Service): Unit = {
      val status = service.take()
      println(status)
      if (status) {
        checkStatus
      }
    }
  }

  object FutureLoop:
    import scala.concurrent._
    import scala.concurrent.duration._

    import ExecutionContext.Implicits.global

    def loopStep(start: Int): Future[Int] =
      Future {
        println(start)
        blocking {
          Thread.sleep(1000)
        }
        start + 1
      }

    def loopFlatMap(start: Int, max: Int): Future[Unit] =
      loopStep(start).flatMap { next =>
        if (next > max) Future.successful(()) else loopFlatMap(next, max)
      }

    def runFL(): Unit = Await.ready(loopFlatMap(1, 10), Duration.Inf)

  @main def runInfiniteLoops(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/FPIntro/InfiniteLoops.scala created at time 2:56PM")
    NaiveTask().run()
    JavaThreadTask().run()

    Iterator
    .continually(summon[Service].take())
    .zipWithIndex
    .takeWhile(_._1 == true)
    .foreach { case (result, counter) =>
      println(s"Counter: ${counter + 1}, Result: $result")
    }

    FutureLoop.runFL()

    println("Done!")
