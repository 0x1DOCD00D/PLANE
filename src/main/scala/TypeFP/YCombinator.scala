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

/*
* (λx.x x)(λx.x x) = (λx.x x)(λx.x x)      (substitute λx.x x into the parameter x)
                   = (λx.x x)(λx.x x)      (at this point we see that it is a nonstop)
* Y = λf.(λx.f (x x))(λx.f (x x)) is called Y combinator with two parameters f and x
* Y g = λf.(λx.f (x x)) (λx.f (x x)) g      (apply Y to g and replace Y with its definition)
    = (λx.g (x x)) (λx.g (x x))             (substitute g into f)
    = g ((λx.g (x x)) (λx.g (x x)))         (substitute (λx.g (x x)) into the parameter x)
    = g (g ((λx.g (x x)) (λx.g (x x))))      (keep repeating ad infinitum)
* Y g = (λx.g (x x)) (λx.g (x x)) = g ((λx.g (x x)) (λx.g (x x))) is the same as g(Yg)
* */

object YCombinator extends App {
  // We need to define the signature/type of the parameter f and depending on its signature we will define what x and g are
  //  Y is a function that takes two parameters, f and x such that Y(f)(x) = f(Y(f))(x)
  //  Without the parameter x the result f(Y(f)) is evaluated lazily to a function that is a fixed point
  //  Let us define the function, f: T => S where T and S are some type variables
  //  The combinator, Y takes the function, f as its parameter, so its signature starts with the definition of a function, T => S as its domain
  //  and then it returns some value that is the function, f applied to the application of Y to f.
  //  A concrete value of the type, T is used as the input to f(Y(f)) and since f: T=>S then Y(f) produces a value of the type, T=>S
  //  and the output is of the type, S
  def apply[T, S](f: (T => S) => (T => S)): T => S = (i: T) => f(apply(f))(i)

  //  (T => S) => (T => S) means that we define some function, someFunction:T => S

  val factFunctionLiteral: Int => Int = (n: Int) => if (n <= 0) 1 else n * factFunctionLiteral(n - 1)
  println(s"Literal: ${factFunctionLiteral(6)}")

  println(apply((factorial: (Int => Int)) => (n: Int) => if (n <= 0) 1 else n * factorial(n - 1)))
  println(apply((factorial: (Int => Int)) => (n: Int) => if (n <= 0) 1 else n * factorial(n - 1))(5))
}
