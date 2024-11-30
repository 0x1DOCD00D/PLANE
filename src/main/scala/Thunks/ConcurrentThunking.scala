
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Thunks

object ConcurrentThunking:

  import java.util.concurrent.Executors
  import scala.concurrent.{Future, ExecutionContext}

  def asyncThunk[A](computation: () => A)(using ec: ExecutionContext): Future[A] =
    Future(computation())

  given ExecutionContext = ExecutionContext.fromExecutor(Executors.newCachedThreadPool())


  @main def runConcurrentThunking(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Thunks/ConcurrentThunking.scala created at time 4:32PM")
    val delayedComputation: [T] => () => T = [T] => () => {
      Thread.sleep(1000)
      "Done deal!".asInstanceOf[T]
    }

    val futureResult = asyncThunk(delayedComputation[String])
    futureResult.foreach(result => println(s"Result: $result")) // Output after 1 second: Result: 42

