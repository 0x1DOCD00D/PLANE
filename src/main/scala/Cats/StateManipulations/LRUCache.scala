
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.StateManipulations

object LRUCache:
  //> using scala "3.3.1"
  //> using dep "org.typelevel:cats-core_3:2.12.0"

  import cats.data.State
  import cats.syntax.all.*

  // -----------------------------
  // LRU model: keep `order` with most-recent at head.
  // When capacity is exceeded, evict the least-recent (tail).
  // -----------------------------
  final case class LRU[K, V](capacity: Int, entries: Map[K, V], order: List[K]) {
    def clampOrder: LRU[K, V] =
      if order.length <= capacity then this
      else {
        val victim = order.last
        copy(entries = entries - victim, order = order.dropRight(1))
      }
  }

  type Cache[K, V, A] = State[LRU[K, V], A]

  // Normalize order so that k becomes Most Recently Used.
  private def touch[K, V](k: K): State[LRU[K, V], Unit] =
    State.modify { s =>
      val newOrder = k :: s.order.filterNot(_ == k)
      s.copy(order = newOrder).clampOrder
    }

  // Put with eviction.
  def put[K, V](k: K, v: V): State[LRU[K, V], Unit] =
    State.modify[LRU[K, V]] { s =>
      val updated = s.copy(entries = s.entries + (k -> v))
      val touched = updated.copy(order = k :: updated.order.filterNot(_ == k))
      touched.clampOrder
    }

  // Get updates recency and returns Option[V].
  def get[K, V](k: K): State[LRU[K, V], Option[V]] =
    for {
      s <- State.get[LRU[K, V]]
      res = s.entries.get(k)
      _ <- res.fold(State.pure[LRU[K, V], Unit](()))(_ => touch[K, V](k))
    } yield res

  // Bulk program: warm cache, then access pattern, then final contents.
  def program: State[LRU[Int, String], (Option[String], Option[String], Map[Int, String], List[Int])] =
    for {
      _ <- put(1, "a") *> put(2, "b") *> put(3, "c") // MRU: 3,2,1
      a <- get(1) // MRU: 1,3,2
      _ <- put(4, "d") // capacity may evict
      b <- get(2) // if evicted, None, else touch
      s <- State.get[LRU[Int, String]]
    } yield (a, b, s.entries, s.order)

  @main def runLRUCache(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Cats/StateManipulations/LRUCache.scala created at time 1:45PM")
    val s0 = LRU[Int, String](capacity = 3, entries = Map.empty, order = Nil)
    val (s1, (a, b, entries, order)) = program.run(s0).value

    println(s"get(1) => $a, get(2) => $b")
    println(s"entries => $entries")
    println(s"order   => $order  // MRU head")

// Behind the scenes:
// - put and get are just S => (S, A). Eviction is pure, reproducible, testable.
// - `*>` sequences transitions, discarding previous A while threading S.

