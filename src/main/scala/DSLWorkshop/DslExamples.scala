/*
 * Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package DSLWorkshop

object DslExamples:
  /*
    create {
      table "users" {
        column "name" varchar(255)
        column "age" int
      }
      table "orders" {
        column "id" int
        column "user_id" int
        column "amount" int
      }
    }
    val result = table "users" select("name", "age") where("age > 21") orderBy("name")
  * */

  /*
    route {
      path("users" / IntNumber) { userId =>
        get {
          complete(fetchUser(userId))
        }
      }
    }
  * */

  /*
    config {
      database {
        url = "jdbc:mysql://localhost:3306/mydb"
        user = "admin"
        password = "secret"
      }
    }
  * */

  /*
    new Order to buy(100 sharesOf "IBM")
            maxUnitPrice 300
            using premiumPricing,

  // use the default pricing strategy
  new Order to buy(200 sharesOf "GOOGLE")
            maxUnitPrice 300
            using defaultPricing,

  // use a custom pricing strategy
  new Order to sell(200 bondsOf "Sun")
            maxUnitPrice 300
            using {
              (qty, unit) => qty * unit - 500
            }
  * */
  @main def runit = println("Hello, world!")
