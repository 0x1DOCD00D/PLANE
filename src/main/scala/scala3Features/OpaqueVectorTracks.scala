////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scala3Features

//https://users.scala-lang.org/t/extending-versus-wrapping-vector-types/10467/6
//If we want to add functionality to some library type, extending it is better than wrapping it into some other type
case class Track(t: Int)
//case class Track(t: Int) extends Vector[Track]

object Tracker:
  opaque type Tracks = Vector[Track]
  object Tracks:
    def apply(tracks: Track*): Tracks = Vector(tracks*)
    extension (tracks: Tracks)
      private def ops: Vex[Track, Tracks] = Vex(tracks)
      export ops.*

class Vex[A, C](c: C)(using C =:= Vector[A]):
  def ++(other: C): C = (c ++ other).asInstanceOf[C]
  def :+(a: A): C = (c :+ a).asInstanceOf[C]

@main def test(): Unit = println:
  import Tracker.*
  val tracks = Tracks(Track(42), Track(27))
  assert(tracks ++ Tracks(Track(5)) == tracks :+ Track(5))
  tracks :+ Track(5)
