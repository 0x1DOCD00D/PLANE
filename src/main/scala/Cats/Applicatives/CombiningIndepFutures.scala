
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.Applicatives

object CombiningIndepFutures:

  import scala.concurrent.{ExecutionContext, Future}
  import cats.syntax.apply.* // tupled/mapN
  import cats.instances.future.given // given Apply/Applicative/Monad[Future]

  final case class Product(id: String, name: String)

  final case class Price(id: String, amount: BigDecimal)

  final case class Inventory(id: String, available: Int)

  final case class Page(product: Product, price: Price, inventory: Inventory)

  def fetchProduct(id: String)(using ExecutionContext): Future[Product] =
    Future(Product(id, "Gizmo"))

  def fetchPrice(id: String)(using ExecutionContext): Future[Price] =
    Future(Price(id, BigDecimal(19.99)))

  def fetchInventory(id: String)(using ExecutionContext): Future[Inventory] =
    Future(Inventory(id, 42))

  def buildPage(id: String)(using ExecutionContext): Future[Page] =
    val fp = fetchProduct(id)
    val fr = fetchPrice(id)
    val fi = fetchInventory(id)
    (fp, fr, fi).mapN(Page.apply)

  @main def runCombiningIndepFutures(): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Cats/Applicatives/CombiningIndepFutures.scala created at time 1:19PM")
    given ExecutionContext = scala.concurrent.ExecutionContext.global

    val pageF = buildPage("12345")
    pageF.foreach(println) // Page(Product(12345,Gizmo),Price(12345,19.99),Inventory(12345,42))
    Thread.sleep(200) // let the Future complete before exit