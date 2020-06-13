/*
 *
 *  * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package DesignPatterns

class StrategyPattern[T, S] {
  //the behavior is called Machinery. It performs some action which is embodied
  // in the function strategy. THis is how this behavior is performed
  def Machinery[T, S](strategy: T => S)(i: T): S = {
    val res = strategy(i)
    println(res)
    res
  }
}

trait Strategy[T, S] {
  def convert(i: T): S
}

class Strategy1 extends Strategy[Int, String] {
  override def convert(i: Int): String = i.toString
}

object StrategyPattern extends App {
  new StrategyPattern[Int, String].Machinery((new Strategy1).convert)(10)
}
