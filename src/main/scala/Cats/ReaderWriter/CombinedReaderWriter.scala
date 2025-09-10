
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.ReaderWriter

object CombinedReaderWriter:

  import cats.data.{ReaderT, Writer}
  import cats.implicits._

  // ---------- Environment / Config ----------
  case class Config(baseDiscount: Double, taxRate: Double)

  case class Env(
                  config: Config,
                  discountService: (Double, Double) => Double,
                  taxService: (Double, Double) => Double
                )

  // ---------- Type Alias for Reader+Writer ----------
  type Log[A] = Writer[List[String], A]
  type ReaderWriter[R, A] = ReaderT[Log, R, A] // ReaderT over Writer
  type App[A] = ReaderWriter[Env, A]

  /*
      type Log[A] = Writer[List[String], A]
      type ReaderWriter[R, A] = ReaderT[Log, R, A]  // ReaderT over Writer
      type App[A] = ReaderWriter[Env, A]

      So every computation in `App[A]` is a function `Env => Writer[List[String], A]`
      Which means it takes an `Env`, and produces both a value of type A and an accumulated log of type List[String]

      def askEnv: App[Env] = ReaderT.ask[Log, Env]
      * `ReaderT.ask` is a standard Cats helper: it extracts the environment `R` and returns it inside the monad.
      * Here, `R = Env` and the outer monad is `Log` (i.e. `Writer[List[String], *]`).
      * So `askEnv` means: “give me the current environment `Env`, with no logs.”

      ReaderT.ask[Log, Env] is equivalent to
        ReaderT[Log, Env, Env](env => Writer.value(env))

      which is just a function that takes `env` and wraps it as a `Writer` value with empty logs.

      env <- askEnv //get the raw Env to use inside your program, with no log messages emitted.

      ---

      def tell(msg: String): App[Unit] =
        ReaderT.liftF(Writer.tell(List(msg)))

      Writer.tell(List(msg)) produces a `Writer[List[String], Unit]` with:
        * The log = `List(msg)`
        * The value = `()`

      `ReaderT.liftF` lifts this `Writer` into the `ReaderT` context.

      * Since `ReaderT` is basically `R => F[A]`, lifting means “ignore the environment, just return this effect.”

      How it works behind the scenes

      * `Writer.tell(List(msg))` is internally Writer(List(msg), ())

      * `ReaderT.liftF` wraps it as ReaderT[Log, Env, Unit](_ => Writer(List(msg), ()))
      So when you use:
      _ <- tell("Applied discount 10.0, result = 90.0")
      you are adding a log message while not producing a meaningful value (just `Unit`).

      Without `askEnv` and `tell`, you would constantly write boilerplate like:

      ReaderT[Log, Env, Env](env => Writer.value(env))   // instead of askEnv
      ReaderT[Log, Env, Unit](_ => Writer.tell(List(msg))) // instead of tell

      The helpers make your for-comprehensions **look clean and expressive**, almost like a custom DSL for “config + logging” computations.
  * */

  def tell(msg: String): App[Unit] =
    ReaderT.liftF(Writer.tell(List(msg)))

  def askEnv: App[Env] = ReaderT.ask[Log, Env]

  /*
    The for-comprehension in `computePrice` does the following steps:
      1. `askEnv` gives us the current `Env` (config + services).
      2. `tell("Computing price...")` adds the first log.
      3. Use the environment to apply discount.
      4. `tell(...)` adds the log about discount.
      5. Apply tax.
      6. `tell(...)` adds the log about tax.
      7. Finally return the computed price as the result.
  * */
  def computePrice(item: String, base: Double): App[Double] =
    for {
      env <- askEnv
      _ <- tell(s"Computing price for $item")
      afterDiscount = env.discountService(base, env.config.baseDiscount)
      _ <- tell(s"Applied discount ${env.config.baseDiscount}, result = $afterDiscount")
      finalPrice = env.taxService(afterDiscount, env.config.taxRate)
      _ <- tell(s"Applied tax ${env.config.taxRate}, result = $finalPrice")
    } yield finalPrice

  given Env = Env(
    Config(10.0, 0.07),
    (price, discount) => price - discount,
    (price, rate) => price * (1 + rate)
  )

  @main def runCombinedReaderWriter(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Cats/ReaderWriter/CombinedReaderWriter.scala created at time 12:23PM")
    // Instead of passing env explicitly, we summon the given
    val (logs, value) = computePrice("Laptop", 100.0).run(summon[Env]).run

    println(s"Final price = $value")
    println("Logs:")
    logs.foreach(println)

