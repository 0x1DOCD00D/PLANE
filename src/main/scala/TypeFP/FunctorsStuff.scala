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

object FunctorsStuff extends App {

  //since it is a functor, it must implement the method map
  //it takes a container as the type variable, C that can be parameterized
  //by some other type and. The method map is parameterized by two type variables, T and S
  //and it takes some function, f that maps T to S and it returns the container, C parameterized
  //by some other type, S
  trait Functor[C[_]] {
    def map[T, S](container: C[T])(f: T => S): C[S]
  }

  //in this example we convert a set of ints to a set of strings
  //using the method map already implemented for the type Set
  val example1 = new Functor[Set] {
    override def map[T, S](container: Set[T])(f: T => S): Set[S] = container.map(f)
  }
  val result = example1.map(Set(1, 2, 3))("string: " + _.toString)
  println(result)
}
