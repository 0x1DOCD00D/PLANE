
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Thunks

object GeneratingThunk:
  class Generator[A](private val thunk: () => Option[(A, Generator[A])]) {
    def next: Option[(A, Generator[A])] = thunk()
  }

  object Generator {
    def range(start: Int, end: Int): Generator[Int] =
      if (start >= end) new Generator(() => None)
      else new Generator(() => Some((start, range(start + 1, end))))
  }
  
  @main def runGeneratingThunk(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Thunks/GeneratingThunk.scala created at time 4:45PM")
    val gen = Generator.range(1, 10)
    val firstFive = LazyList.unfold(gen)(_.next).take(5).toList
    println(firstFive) // Output: LazyList(1, 2, 3, 4, 5)

    
