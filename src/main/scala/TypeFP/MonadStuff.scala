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

object MonadStuff {

  //we defined the type Functor as a generalized container
  //it contains some value of the type expressed in the type variable, T
  //the function, f maps the the type T to some other type, S and
  //the method map returns a new container that contains a value of the type, S
  trait Functor[T] {
    def map[S](f: T => S): Functor[S]
  }

  //we define the monadic pattern
  trait Monad[T] extends Functor[T] {
    //the method unit wraps a value of some type, S into the monad type
    def unit[S](value: S): Monad[S]

    //unlike the method map, this method, flatMap takes the function, f
    //as its input that converts the value of the type, T into a monad
    //that hold a value of the type, S
    def flatMap[S](f: T => Monad[S]): Monad[S]

    //and now we are back to defining the function, map
    //the difference with the method flatMap is that some work
    //needs to be done to wrap the value of the type, S into Monad
    //and we have the method unit for that.
    override def map[S](f: T => S): Monad[S] = flatMap((storedValue: T) => unit(f(storedValue)))
  }

}
