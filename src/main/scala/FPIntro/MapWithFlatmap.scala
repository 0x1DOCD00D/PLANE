
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package FPIntro

object MapWithFlatmap:
  @main def runMapWithFlatmap(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/FPIntro/MapWithFlatmap.scala created at time 1:16PM")

    val list = List(1, 2, 3)
    val res = list.map(x => List(x, x * 2))
    println(res) // Output: List(List(1, 2), List(2, 4), List(3, 6))

    val resFM = list.flatMap(x => List(x, x * 2))
    println(resFM) // Output: List(1, 2, 2, 4, 3, 6)

    val someVal: Option[Int] = Some(2)

    val resX2 = someVal.flatMap(x => Some(x * 2))
    println(resX2) // Output: Some(4)

    val mappedResult = someVal.map(x => Some(x * 2))
    println(mappedResult) // Output: Some(Some(4))

    // Flatten required (no direct flatten for Option)
    val flattenedResult = mappedResult.flatten
    println(flattenedResult) // Output: Some(4)


    val nested = List(List(1, 2), List(3, 4))
    // Use `flatMap` to flatten the lists, then `map` to transform
    val flattenedAndDoubled = nested.flatMap(_.map(_ * 2))
    println(flattenedAndDoubled) // Output: List(2, 4, 6, 8)

    val someValue: Option[Int] = Some(10)
    val noneValue: Option[Int] = None
    // Use `flatMap` to chain computations, avoiding nested Options
    val result = someValue.flatMap(x => Some(x * 2)).map(_ + 1)
    println(result) // Output: Some(21)
    // Combining multiple Options
    val combined = someValue.flatMap(x => noneValue.map(y => x + y))
    println(combined) // Output: None

    val opt1: Option[Int] = Some(10)
    val opt2: Option[Int] = Some(20)
    
    val liftedAdd = (a: Int, b: Int) => a + b
    
    // Using map and flatMap
    val resY = opt1.flatMap(a => opt2.map(b => liftedAdd(a, b)))
    println(resY) // Output: Some(30)
    
    import cats.implicits._
    val combCats = (opt1, opt2).mapN(_ + _)
    println(combCats) // Output: Some(30)

    import cats.implicits._

    // Example: Converting List[Option[Int]] to Option[List[Int]]
    val listOfOptions: List[Option[Int]] = List(Some(1), Some(2), Some(3))
    val optionOfList: Option[List[Int]] = listOfOptions.traverse(identity)
    println(optionOfList) // Output: Some(List(1, 2, 3))

    import scala.concurrent.Future
    import scala.concurrent.ExecutionContext.Implicits.global

    val futureOption: Future[Option[Int]] = Future.successful(Some(10))

    // Flattening Future[Option[Int]] to Future[Int]
    val r = futureOption.flatMap {
      case Some(value) => Future.successful(value)
      case None => Future.failed(new Exception("No value"))
    }
    println(s"Future value is ${r.value}")
    




