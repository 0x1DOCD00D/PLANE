/*
 *
 *  * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package InterestingScalaFeatures

object AmbiguityOfTHIS extends App {

  //my own garden variety function type
  trait MyFunction[-T, +S] {
    def apply(p: T): S
  }

  //and its instantiation, nothing important
  new MyFunction[Int, String] {
    override def apply(p: Int): String = p.toString
  }.apply(3).foreach(println)

  //I want to extend this function with the composition functionality
  trait MyFunctionComposition[-T, +S] extends MyFunction[T, S] {
    //explained below
    private val self = this

    override def apply(p: T): S

    def compose[V](nextFunction: MyFunctionComposition[S, V]): MyFunction[T, V] = {
      new MyFunction[T, V] {
        override def apply(p: T): V = {
          //Type mismatch.
          //Required: S
          //Found: V
          //dem it!
          //val result = nextFunction.apply(this.apply(p))
          //we need to use the variable this that references the outer instance
          //but it is shadowed by the variable this in the inner instance
          //let us introduce a new variable called self
          val result = nextFunction.apply(self.apply(p))
          result
        }
      }
    }
  }

  new MyFunctionComposition[Float, Int] {
    override def apply(p: Float): Int = p.toInt
  }.compose[String](new MyFunctionComposition[Int, String] {
    override def apply(p: Int): String = p.toString
  }).apply(3.25f).foreach(println)

}
