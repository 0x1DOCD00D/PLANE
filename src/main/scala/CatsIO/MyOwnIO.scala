
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO

// A minimal effect that delays a side effect until you run it.
final case class MyIO[A](unsafeRun: () => A):
  def map[B](f: A => B): MyIO[B]       = MyIO(() => f(unsafeRun()))
  def flatMap[B](f: A => MyIO[B]): MyIO[B] =
    MyIO(() => f(unsafeRun()).unsafeRun())

object MyIO:
  def delay[A](thunk: => A): MyIO[A]   = MyIO(() => thunk)

@main def runMyOwnIO(args: String*): Unit =
  println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/CatsIO/MyOwnIO.scala created at time 1:13PM")

  val program: MyIO[Unit] =
    for
      _ <- MyIO.delay(println("hello"))
      _ <- MyIO.delay(println("world"))
    yield ()

  program.unsafeRun()

  MyIO.delay(println("hello")).flatMap(e1 => MyIO.delay(println("world")).map(e2=>())).unsafeRun()
