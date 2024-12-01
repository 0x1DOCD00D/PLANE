
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Thunks

object Fibonacci:
  def fibonacci(n: BigInt): BigInt = {
    // Define the infinite Fibonacci sequence as a LazyList
    lazy val fib: LazyList[BigInt] = BigInt(0) #:: BigInt(1) #:: fib.zip(fib.tail).map { case (a, b) => a + b }
    fib.take((n + 1).toInt).last
  }

  val fibs =
    def go(current: BigInt, next: BigInt): LazyList[BigInt] = current #:: go(next, current + next)
    go(0, 1)


  @main def runFibonacci(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Thunks/Fibonacci.scala created at time 4:13PM")
    println(fibonacci(100)) 
    println{
      fibs.take(102).toList
    }
