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

class LazyPattern {
  //this instance variable will not be evaluated until explicitly invoked
  lazy val lazycontent = 1 to 100 foreach (println)
  //however, this instance variable will be automatically evaluated as part of instance creation
  val content = 1 to 10 foreach (println)
}

object DoIt extends App {
  println("It is the eager time!")
  val obj = new LazyPattern
  println("It is the lazy time!")
  obj.lazycontent
}
