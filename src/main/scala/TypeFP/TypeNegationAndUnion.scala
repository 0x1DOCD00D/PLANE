////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package TypeFP
//refactored from https://michid.wordpress.com/

object TypeNegationAndUnion:
  type not[A] = A => Nothing

  // De Morgan's law for the union of two types
  type v[T, U] = not[not[T] with not[U]] 

  type notNot[A] = not[not[A]]
  type V[T, U] = [X] =>> notNot[X] <:< v[T, U]

  def size[T: V[Int, String]](t: T): Int = t match {
    case i: Int    => i
    case s: String => s.length
  }

  @main def runTypeNegationAndUnion(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/TypeFP/TypeNegationAndUnion.scala created at time 7:37PM")
    println(size(1))
    println(size("abc"))
