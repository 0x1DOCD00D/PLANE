/*
 *
 *  * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package TypeFP

import java.util.ArrayList

object Functor4ArrayList {

  trait Functor[F[_]] {
    def map[A, B](f: A => B): F[A] => F[B]
  }


/*
  implicit object JAL_a_Functor extends Functor[ArrayList] {
    def map[X, Y](f: X => Y) = (xs: ArrayList[X]) => {
      val ys = new ArrayList[Y]
      for (i <- 0 until xs.size) ys.add(f(xs.get(i)))
      ys
    }
  }

  implicit def fops[F[_] : Functor, A](fa: F[A]): Functor[ArrayList] = new {
    val witness = implicitly[Functor[F]]

    final def map[B](f: A => B): F[B] = witness.map(f)(fa)
  }


  val testList = new ArrayList(java.util.Arrays.asList("this", "is", "a", "test"))
  val transformed = testList.map(s => s.asInstanceOf[String].toUpperCase)

  def main(args: Array[String]): Unit = {
    println(transformed)
  }
*/
}
