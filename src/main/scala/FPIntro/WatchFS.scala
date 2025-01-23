////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package FPIntro

import java.nio.file.{FileSystems, Path, Paths, StandardWatchEventKinds, WatchEvent, WatchKey}
import scala.concurrent.duration.Duration
import scala.concurrent.{blocking, Await, ExecutionContext, ExecutionContextExecutor, Future}
import scala.jdk.CollectionConverters.*
import scala.util.{Failure, Success, Try}

object WatchFS:
  val home = Paths.get(".").toAbsolutePath
  val watchService = FileSystems.getDefault.newWatchService()
  home.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY)

  given executionContext: ExecutionContextExecutor = ExecutionContext.global

  def takeKey(): Future[WatchKey] = Future {
    blocking {
      watchService.take()
    }
  }

  def resetKey(watchKey: WatchKey): Future[Boolean] = Future {
    watchKey.reset()
  }

  def eventPaths(watchKey: WatchKey): List[Path] = {
    watchKey
      .pollEvents()
      .asInstanceOf[java.util.List[WatchEvent[Path]]]
      .asScala
      .toList
      .map(_.context())
  }

  def printPaths(paths: List[Path]): Future[Unit] = Future {
    paths.foreach(println)
  }

  def watchStep(): Future[Boolean] =
    for {
      key <- takeKey()
      paths = eventPaths(key)
      _    <- printPaths(paths)
      cont <- resetKey(key)
    } yield cont

  def watch(): Future[Unit] =
    for {
      cont <- watchStep()
      next <- if (cont) watch() else Future.successful(())
    } yield next

  println(home)

  Await.ready(watch(), Duration.Inf)

  @main def runWatchFS(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/FPIntro/WatchFS.scala created at time 3:22PM")
