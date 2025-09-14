
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.Semigroupal

import cats.Semigroupal
import cats.instances.list.*

object SKU_Catalog:
  import cats.syntax.apply.*

  val colors = List("red", "blue")
  val sizes = List("S", "M", "L")
  val regions = List("US", "EU")

  // All combinations (2 * 3 * 2 = 12)
  val combos: List[(String, String, String)] = (colors, sizes, regions).tupled

  // Derive useful data in one pass with mapN:
  val skus: List[String] = (colors, sizes, regions).mapN { (c, s, r) => s"SKU-$c-$s-$r" }

  // If you want the raw Semigroupal:
  val raw: List[((String, String), String)] =
    Semigroupal[List].product(Semigroupal[List].product(colors, sizes), regions)

  @main def runSKU_Catalog(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Cats/Semigroupal/SKU_Catalog.scala created at time 1:05PM")
    println(s"All combinations: $combos")
    println(s"SKUs: $skus")
    println(s"Raw: $raw")
