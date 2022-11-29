package Cats

import cats.{Eval, Foldable}

import scala.language.postfixOps

/*
 *
 *  Copyright (c) 2021. Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *   
 *   Unless required by applicable law or agreed to in writing, software distributed under
 *   the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *   either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *  
 */

object EvalMonad:
  /*
  * val Now eager, memoized
    def Always lazy, not memoized
    lazy val Later lazy, memoized
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
  //    println(composedFuncBaad(0))//blows up the stack

  /*
  * We define our own composer method that defers the evaluation
  * of the composed function in a monad that is implemented using trampoline
  * so that it is stack-safe. The composed function's evaluation is deferred
  * and it is going to be invoked only when the thunk is called explicitly.
  * */
  def composeEvals(f: Int=>Eval[Int], g: Int=>Eval[Int]): Int=>Eval[Int] =
      (i:Int)=> Eval.defer{ f(i).flatMap(x=> g(x))}

  @main def runMain_EvalMonad$(): Unit =
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
