
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.ReaderWriter

object WriterWithGiven:

  import cats.data.Writer
  import cats.implicits._

  // ---------- Environment / Config ----------
  case class Config(baseDiscount: Double, taxRate: Double)

  // Environment with lambdas as services
  case class Env(
                  config: Config,
                  discountService: (Double, Double) => Double,
                  taxService: (Double, Double) => Double
                )

  // ---------- Type Alias ----------
  type Log[A] = Writer[List[String], A]

  // ---------- Program as a context function ----------
  def computePrice(item: String, base: Double)(using env: Env): Log[Double] =
    for {
      _ <- Writer.tell(List(s"Computing price for $item"))
      afterDiscount = env.discountService(base, env.config.baseDiscount)
      _ <- Writer.tell(List(s"Applied discount ${env.config.baseDiscount}, result = $afterDiscount"))
      finalPrice = env.taxService(afterDiscount, env.config.taxRate)
      _ <- Writer.tell(List(s"Applied tax ${env.config.taxRate}, result = $finalPrice"))
    } yield finalPrice

  // ---------- Given Environment ----------
  given Env = Env(
    Config(10.0, 0.07),
    (price, discount) => price - discount, // discount lambda
    (price, rate) => price * (1 + rate) // tax lambda
  )

  @main def runWriterWithGiven(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Cats/ReaderWriter/WriterWithGiven.scala created at time 12:42PM")
    val (logs, value) = computePrice("Laptop", 100.0).run
    println(s"Final price = $value")
    println("Logs:")
    logs.foreach(println)
