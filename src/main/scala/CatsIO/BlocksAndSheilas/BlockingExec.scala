
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.BlocksAndSheilas

import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits.*
import CatsIO.Helpers.Aid4Debugging.*
import CatsIO.Helpers.{blue, bold, green, red}

import scala.util.{Failure, Success, Try}
import java.util.concurrent.{Executors, LinkedBlockingQueue, ScheduledExecutorService, ThreadFactory, ThreadPoolExecutor, TimeUnit}
import scala.concurrent.ExecutionContext
import cats.effect.unsafe.{IORuntime, IORuntimeConfig, Scheduler}

def daemonThreadFactory(name: String): ThreadFactory =
  new ThreadFactory {
    private val default = Executors.defaultThreadFactory()
    def newThread(r: Runnable): Thread = {
      val t = default.newThread(r)
      t.setName(name + "-" + t.getId)
      t.setDaemon(true)
      t
    }
  }

object BlockingExec extends IOApp:
  private val computeExecutor = Executors.newFixedThreadPool(8, daemonThreadFactory("compute"))
  private val blockingExecutor = Executors.newFixedThreadPool(16, daemonThreadFactory("blocking"))
  private val schedulerExecutor: ScheduledExecutorService =
    Executors.newScheduledThreadPool(1, daemonThreadFactory("scheduler"))

  // wrap into ExecutionContexts / Scheduler
  private val computeEC: ExecutionContext = ExecutionContext.fromExecutorService(computeExecutor)
  private val blockingEC: ExecutionContext = ExecutionContext.fromExecutorService(blockingExecutor)
  private val scheduler: Scheduler = Scheduler.fromScheduledExecutor(schedulerExecutor)

  // shutdown hook (called automatically when IOApp exits)
  private val shutdown: () => Unit = () => {
    computeExecutor.shutdown()
    blockingExecutor.shutdown()
    schedulerExecutor.shutdown()
  }

  // provide custom runtime
  given IORuntime = IORuntime(
    compute = computeEC,
    blocking = blockingEC,
    scheduler = scheduler,
    shutdown = shutdown,
    config = IORuntimeConfig()
  )
  def log[T](message: String, instance: T): T =
    println(message + ": " + instance.toString)
    instance
  end log

  def BlockingExec_Program: IO[Unit] = for {
    _ <- IO.println(s"Step 1: ${Thread.currentThread.getName}".blue.bold).debugInfo()
    _ <- IO.println(s"Step 2: ${Thread.currentThread.getName}".blue.bold).debugInfo()
    fib <- IO(println("/Users/drmark/IdeaProjects/PLANE/src/main/scala/CatsIO/BlocksAndSheilas/BlockingExec.scala".red)).start
    _ <- IO.blocking("on blocker inside BlockingExec_Program".green).debugInfo().evalOn(blockingEC)
    _ <- IO.println("on default inside BlockingExec_Program").debugInfo().evalOn(computeEC)
    l1 = log("time: 9/28/25:", fib.toString)
    _ <- fib.join
  } yield ()

  def withBlocker: IO[Unit] =
      for {
        _ <- IO.println("on default").debugInfo().evalOn(computeEC)
        res <- IO.blocking {
          println("on blocker inside withBlocker".green)
        }.debugInfo().evalOn(blockingEC)
        _ <- IO.println("where am I?").debugInfo()
      } yield res

  override def run(args: List[String]): IO[ExitCode] =
      withBlocker.as(ExitCode.Success) *> BlockingExec_Program.as(ExitCode.Success)
