////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scala3Features

object NamedTupleOrdering {
  import scala.NamedTuple.NamedTuple

  given orderingForNamedTuple[N <: Tuple, V <: Tuple](using ord: Ordering[V]): Ordering[NamedTuple[N, V]] with
    def compare(x: NamedTuple[N, V], y: NamedTuple[N, V]): Int =
      ord.compare(x.toTuple, y.toTuple)

  type Person = (name: String, age: Int)

  val so: orderingForNamedTuple[("name", "age"), (String, Int)] = summon[Ordering[Person]]
  val people: List[Person] = List(
     ("Charlie", 35),
     ("Alice", 30),
     ("Alice", 29),
     ("Bob", 25)
  )
  val sortedPeople: Seq[Person] = people.sorted

  @main def main = println(s"$sortedPeople")

}
