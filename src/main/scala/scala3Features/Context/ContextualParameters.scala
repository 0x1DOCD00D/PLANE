////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scala3Features.Context

//https://users.scala-lang.org/t/support-when-only-1-overload-has-contextual-parameters/10870

object ContextualParameters {
  def f(x: Int) = "a"

  def f(x: (n: Int) ?=> Int) = "b"

  f(10) // "a" - selects the first overload
  // f(n * 3) // error - Not found 'n'

  def main(args: Array[String]): Unit = {
    object X {
      def f(x: (n: Int) ?=> Int) = x(using 42);

      def f(x: Int) = (x * 2).toString
    }
    X.f(3)

    object X1 {
      def f(x: (n: Int) => Int) = x(42);

      def f(x: Int) = (x * 2).toString
    }

    X1.f(_ + 1)
    val res1: Int = 43
  }
}
