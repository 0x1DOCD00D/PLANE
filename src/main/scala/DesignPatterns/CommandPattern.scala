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

import scala.collection.mutable.ListBuffer

class CommandPattern[T, S] {
  private val commandList: ListBuffer[T => S] = ListBuffer()

  def add(c: T => S) = commandList += c

  def execute(i: T) = commandList.foreach(c => c(i))
}

object CommandPattern extends App {
  def c1(i: Int) = println(i + 1)

  def c2(i: Int) = println(-i)

  val comList = new CommandPattern[Int, Unit]
  comList.add(c1)
  comList.add(c2)
  comList.execute(5)
}
