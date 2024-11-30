
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Thunks

object LazyListX:
  enum LazyList[+A]:
    case Empty
//    Cons data constructor takes explicit thunks (() => A and ()=> LazyList[A]
    case Cons(h: () => A, t: () => LazyList[A])

  object LazyList:
    def cons[A](
                 hd: => A, tl: => LazyList[A]
               ): LazyList[A] =
      lazy val head = hd
      lazy val tail = tl
      Cons(() => head, () => tail)

    def empty[A]: LazyList[A] = Empty

    def apply[A](as: A*): LazyList[A] =
      if as.isEmpty then empty
      else cons(as.head, apply(as.tail *))

  @main def runLazyListX(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/FPIntro/LazyListX.scala created at time 3:22PM")
    val ll = LazyList(1, "a", (Symbol("a"), 2.5f))
    println {
      ll match
        case LazyList.Empty => "no elements in the lazy list"
        case LazyList.Cons(h, t) => s"${h().toString()} followed by ${t().toString()}"
    }
