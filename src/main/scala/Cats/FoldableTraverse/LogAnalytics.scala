
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.FoldableTraverse

object LogAnalytics:

  import cats.*
  import cats.implicits.*

  final case class Log(service: String, bytes: Long, ok: Boolean)

  val batch: List[Log] =
    List(Log("auth", 321, true), Log("auth", 200, false), Log("api", 900, true))

  // a) Bytes per service (Monoid[Map] + foldMap)
  val bytesPerService: Map[String, Long] =
    batch.foldMap(l => Map(l.service -> l.bytes)) // Map(auth -> 521, api -> 900)

  // b) Overall success/failure tallies (user-defined Monoid)
  final case class Tallies(success: Long, failure: Long)

  object Tallies {
    given Monoid[Tallies] = Monoid.instance(Tallies(0, 0), (a, b) => Tallies(a.success + b.success, a.failure + b.failure))
  }

  val tallies: Tallies =
    batch.foldMap(l => if (l.ok) Tallies(1, 0) else Tallies(0, 1)) // Tallies(2,1)
    
  @main def runLogAnalytics(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Cats/FoldableTraverse/LogAnalytics.scala created at time 11:12AM")
    println(s"Bytes per service: $bytesPerService")
    println(s"Tallies: $tallies")
    /*
    Bytes per service: Map(auth -> 521, api -> 900)
    Tallies: Tallies(2,1)
    * */
