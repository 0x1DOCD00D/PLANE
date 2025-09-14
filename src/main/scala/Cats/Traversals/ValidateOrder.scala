
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.Traversals

import cats.data.{Validated, ValidatedNel}
import cats.syntax.all.*

object ValidateOrder:
  final case class OrderLine(sku: String, qty: Int)

  type VE[A] = ValidatedNel[String, A]

  def validateLine(cols: Map[String, String]): VE[OrderLine] = {
    def need(k: String) = cols.get(k).filter(_.nonEmpty).toValidNel(s"Missing '$k'")

    def parseQty(s: String) =
      Either.catchOnly[NumberFormatException](s.toInt)
        .leftMap(_ => s"Bad qty: $s").toValidatedNel
        .andThen(q => Validated.condNel(q > 0, q, s"qty must be > 0, was $q"))

    (need("sku"), need("qty").andThen(parseQty)).mapN(OrderLine.apply)
  }

  def validateAll(rows: List[Map[String, String]]): VE[List[OrderLine]] =
    rows.traverse(validateLine)

  @main def runValidateOrder(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Cats/Traversals/ValidateOrder.scala created at time 2:07PM")
    val data = List(
      Map("sku" -> "A123", "qty" -> "2"),
      Map("sku" -> "", "qty" -> "3"),
      Map("sku" -> "C789", "qty" -> "-1"),
      Map("sku" -> "D012", "qty" -> "xyz")
    )
    val result = validateAll(data)
    println(result)
