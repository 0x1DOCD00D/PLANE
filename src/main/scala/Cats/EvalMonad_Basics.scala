
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats

import cats.{Eval, Foldable}

import scala.language.postfixOps

object EvalMonad_Basics:
  /*
  * val Now eager, memoized
    def Always lazy, not memoized
    lazy val Later lazy, memoized

 Eval.now(x)      Eval.later(x)           Eval.always(x)
-------------------------------------------------------------
Eager            Lazy (memoized)         Lazy (not memoized)
Value computed   Value computed once     Value recomputed
immediately      on first access,        every time
                 then cached

val a = Eval.now(1 + 2)         val b = Eval.later{ println("calc"); 1+2 }
println(a.value) // 3           println(b.value) // prints "calc", then 3
println(a.value) // 3           println(b.value) // just 3, no "calc"

                 val c = Eval.always{ println("calc"); 1+2 }
                 println(c.value) // prints "calc", 3
                 println(c.value) // prints "calc", 3 again

  * */


  val lstOfFuncz1000: List[Int => Int] = List.fill(1000)((i: Int) => i + 1)

  val composedFuncOk: Int => Int = lstOfFuncz1000.foldLeft((i: Int) => i) {
    (acc, f) => acc andThen f
  }
  println(composedFuncOk(0))

  val lstOfFunczMil: List[Int => Int] = List.fill(1_000_000)((i: Int) => i + 1)

  val composedFuncBaad: Int=>Int = lstOfFunczMil.foldLeft((i: Int) => i) {
    (acc, f) => acc andThen f
  }
//  println(composedFuncBaad(0))//blows up the stack

  /*
  * We define our own composer method that defers the evaluation
  * of the composed function in a monad that is implemented using trampoline
  * so that it is stack-safe. The composed function's evaluation is deferred
  * and it is going to be invoked only when the thunk is called explicitly.
  * */
  def composeEvals(f: Int=>Eval[Int], g: Int=>Eval[Int]): Int=>Eval[Int] =
      (i:Int)=> Eval.defer{ f(i).flatMap(x=> g(x))}

  @main def runMain_EvalMonad(): Unit =
//  from the cats documentation: Defer a computation which produces an Eval[A] value
//  This is useful when you want to delay execution of an expression which produces an Eval[A] value.
//  Like .flatMap, it is stack-safe. Because Eval guarantees stack-safety, we can chain a lot of
//  computations together using flatMap without fear of blowing up the stack.
    val feval: Int => Eval[Int] = (i: Int) => Eval.defer {
      Eval.later(i + 1)
    }
//  apply the function in a deferred monadic way and then get the value
    val res = feval(7).map(x =>
      println(s"deferred eval ${x.intValue}")
      x
    )
    println(res.value)

//    def foldRight[A, B](fa: F[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B]
//    val withFoldable: Eval[Int => Int] = Foldable[List].foldRight(lstOfFunczMil, Eval.now((i:Int)=>i))((f, acc)=>acc.flatMap(g=>Eval.later((i:Int)=>g(f(i))))) //acc.map(x=>Eval.defer(f.andThen(x))))
//    println(withFoldable.value(0))

//    compose two functions or a function with itself
    val compevals = composeEvals(feval,feval)
    println(compevals(0).value)
//    create a list of a million functions
    val lstOfFunczMilEval = List.fill(1_000_000)(feval)
//  compute a composition of these functions
    val composedListFunc = lstOfFunczMilEval.foldLeft(feval) {
      (acc, f) => composeEvals(acc,f)
    }
//  the evaluation is stack safe and it works just fine
    println(composedListFunc(0).value)

//    examples from the cats documentation
    val always = Eval
      .always {
        println("the first step is always")
        "one"
      }
      .map { str => println("next, doing mapping"); s"$str and two" }

    println(always.value)
    println(always.value)

    println(
      Eval
        .always {
          println("Step 1")
          "Hello"
        }
        .map { str => println("Step 2"); s"$str world" }
        .value
    )

    val ans = for {
      a <- Eval.now {
        println("Calculating A")
        40
      }
      b <- Eval.always {
        println("Calculating B")
        2
      }
    } yield {
      println("Adding A and B")
      a + b
    }
    println(ans.value)
    println(ans.value)
    println(ans.value)

    val saying = Eval
      .always {
        println("Step 1")
        "The cat"
      }
      .map { str => println("Step 2"); s"$str sat on" }
      .memoize
      .map { str => println("Step 3"); s"$str the mat" }
    println(s"[1]: ${saying.value}")
    println(s"[2]: ${saying.value}")
    println(s"[3]: ${saying.value}")
