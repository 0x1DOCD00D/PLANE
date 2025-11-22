////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.Concepts

import cats.effect.*
import cats.effect.syntax.all.*
import cats.syntax.all.*

import scala.concurrent.duration.*
import scala.concurrent.{ExecutionContext, Future, Promise}
import scala.util.{Random, Try}
import java.util.concurrent.{Executors, TimeUnit}

/**
 * Comprehensive test suite demonstrating all major IO methods in Cats Effect 3.x
 * Each test highlights what makes that particular method unique
 *
 # Cats Effect IO Methods Test Suite

 This repository contains comprehensive test programs and documentation for understanding the core functionality of each IO method in Cats Effect 3.

 ### Prerequisites
 - Scala 3.x
 - SBT 1.x

## Key Concepts Demonstrated

 ### Creation Methods
 - `IO.apply` / `IO.delay` - Lazy suspension of side effects
 - `IO.pure` - Zero-cost wrapping of pure values
 - `IO.async` - Callback API integration
 - `IO.blocking` - Blocking operations on dedicated thread pool
 - `IO.never` - Semantic "never completes"

 ### Error Handling
 - `attempt` - Convert errors to Either
 - `handleErrorWith` - Type-based error recovery
 - `redeem` - Unify error and success paths

 ### Concurrency
 - `start` - Create controllable fibers
 - `race` - First-to-complete with auto-cancellation
 - `both` - Parallel execution with both results
 - `parMapN` - Applicative parallel composition

 ### Resource Management
 - `bracket` - Full lifecycle management (instance method in CE3)
 - `Resource` - Composable resource management (preferred)
 - `guarantee` - Simple finalizers
 - `onCancel` - Cancellation handlers

 ### State Management
 - `Ref` - Thread-safe atomic references
 - `Deferred` - One-time settable promises
 - `memoize` - Share computation across fibers

 ## Understanding the Tests

 Each test is designed to highlight what makes that particular IO method unique:

 1. **Complete Test Suite** (`IOMethodsTestSuite`) - Shows practical usage of each method with explanatory comments
 2. **Minimal Tests** (`MinimalIOMethodTests`) - Proves each method's unique capability with the smallest possible example
 3. **Comparison Guide** - Explains when to choose each method over alternatives

 ## Cats Effect 3 vs 2

 This code uses Cats Effect 3 syntax. Key differences from CE2:
 - `bracket` is an instance method: `acquire.bracket(use)(release)`
- `Resource` is preferred for resource management
 - No explicit `ContextShift` or `Timer` needed (implicit Runtime)
 - Different parallel execution model

 ## Further Reading

 - [Cats Effect Documentation](https://typelevel.org/cats-effect/)
 - [Cats Effect 3 Migration Guide](https://typelevel.org/cats-effect/docs/migration-guide)
 - [IO Data Type](https://typelevel.org/cats-effect/docs/datatypes/io)
 */
object IOMethodsTestSuite extends IOApp.Simple {

  // =====================================
  // 1. CREATION METHODS
  // =====================================

  /**
   * IO.apply / IO.delay - Suspends a synchronous side effect
   * Unique: Lazily captures side effects, ensuring referential transparency
   */
  def testApplyDelay: IO[Unit] = {
    println("Creating IO with apply/delay...")

    // This won't print until the IO is run
    val lazyEffect: IO[Int] = IO {
      println("  This side effect is suspended!")
      42
    }

    // Equivalent using delay
    /** from the documentation
     * Suspends a synchronous side effect in `IO`. Use [[IO.delay]] if your side effect is not
     * thread-blocking; otherwise you should use [[IO.blocking]] (uncancelable) or
     * `IO.interruptible` (cancelable).
     *
     * Any exceptions thrown by the effect will be caught and sequenced into the `IO`.
     */
    val delayedEffect: IO[String] = IO.delay {
      println("  Delayed effect executed!")
      "result"
    }

    for {
      _ <- IO.println("  Before running effects")
      result1 <- lazyEffect
      result2 <- delayedEffect
      _ <- IO.println(s"  Results: $result1, $result2")
    } yield ()
  }

  /**
   * IO.pure - Lifts a pure value into IO context
   * Unique: For already computed values, no suspension needed
   * WARNING: Never use with side effects!
   */
  def testPure: IO[Unit] = {
    println("\nTesting IO.pure...")

    // Correct usage - pure value
    val pureValue: IO[Int] = IO.pure(42)

    // WRONG - this executes immediately!
    // val wrong = IO.pure(println("This runs immediately!"))

    // Demonstration of eager evaluation
    val eagerValue: Int = {
      println("  Computing value eagerly")
      100
    }

    /* from the documentation
    * This should ''only'' be used if the value in question has "already" been computed! In other
   * words, something like `IO.pure(readLine)` is most definitely not the right thing to do!
   * However, `IO.pure(42)` is correct and will be more efficient (when evaluated) than
   * `IO(42)`, due to avoiding the allocation of extra thunks.
   * */
    val ioPure: IO[Int] = IO.pure(eagerValue) // Value already computed

    for {
      _ <- IO.println("  Before accessing pure value")
      value <- ioPure
      _ <- IO.println(s"  Pure value: $value")
    } yield ()
  }

  /**
   * IO.raiseError - Creates a failed IO
   * Unique: Represents recoverable errors in the IO type system
   */
  def testRaiseError: IO[Unit] = {
    println("\nTesting IO.raiseError...")

    val failedIO: IO[Int] = IO.raiseError(new Exception("Planned failure"))

    val recovered: IO[Int] = failedIO.handleError { error =>
      println(s"  Caught error: ${error.getMessage}")
      -1
    }

    for {
      result <- recovered
      _ <- IO.println(s"  Recovered with value: $result")
    } yield ()
  }

  /**
   * IO.async - Wraps callback-based async operations
   * Unique: Bridges callback-style APIs to IO
   */
  def testAsync: IO[Unit] = {
    println("\nTesting IO.async...")

    def callbackOperation: IO[String] = IO.async { (callback: Either[Throwable,String]=>Unit) =>
      IO {
        // Simulate async callback
        val thread = new Thread(() => {
          Thread.sleep(100)
          callback(Right("Async result"))
        })
        thread.start()

        // Return cancelation token (optional)
        Some(IO(thread.interrupt()))
      }
    }

    for {
      _ <- IO.println("  Starting async operation")
      result <- callbackOperation
      _ <- IO.println(s"  Async result: $result")
    } yield ()
  }

  /**
   * IO.blocking - Runs blocking operations on dedicated thread pool
   * Unique: Prevents blocking operations from starving the compute pool
   */
  def testBlocking: IO[Unit] = {
    println("\nTesting IO.blocking...")

    val blockingOperation = IO.blocking {
      println(s"  Blocking on thread: ${Thread.currentThread().getName}")
      Thread.sleep(100) // Simulating blocking I/O
      "Blocking result"
    }

    for {
      _ <- IO.println("  Starting blocking operation")
      result <- blockingOperation
      _ <- IO.println(s"  Result: $result")
    } yield ()
  }

  /**
   * IO.defer - Suspends an IO-returning computation
   * Unique: Enables stack-safe recursion and lazy IO composition
   */
  def testDefer: IO[Unit] = {
    println("\nTesting IO.defer...")

    def recursiveCountdown(n: Int): IO[Unit] = {
      if (n <= 0) IO.println("  Blastoff!")
      else IO.defer {
        IO.println(s"  $n...") >> recursiveCountdown(n - 1)
      }
    }

    recursiveCountdown(5)
  }

  /**
   * IO.never - Creates an IO that never completes
   * Unique: Useful for keeping programs alive or testing timeouts
   */
  def testNever: IO[Unit] = {
    println("\nTesting IO.never...")

    val neverEnding = IO.never[Int]
    val withTimeout = neverEnding.timeoutTo(
      100.millis,
      IO.println("  Timed out as expected") >> IO.pure(-1)
    )

    for {
      result <- withTimeout
      _ <- IO.println(s"  Result after timeout: $result")
    } yield ()
  }

  // =====================================
  // 2. TRANSFORMATION METHODS
  // =====================================

  /**
   * map - Transforms successful values
   * Unique: Pure transformation without effects
   */
  def testMap: IO[Unit] = {
    println("\nTesting map...")

    val io = IO.pure(10)
    val transformed = io.map(_ * 2)

    for {
      result <- transformed
      _ <- IO.println(s"  Mapped result: $result")
    } yield ()
  }

  /**
   * flatMap - Sequential composition of effects
   * Unique: Enables sequential effect chaining and for-comprehensions
   */
  def testFlatMap: IO[Unit] = {
    println("\nTesting flatMap...")

    val first = IO {
      println("  First effect")
      10
    }

    val chained = first.flatMap { value =>
      IO {
        println(s"  Second effect with value: $value")
        value * 2
      }
    }

    chained.void
  }

  /**
   * flatten - Removes one layer of IO nesting
   * Unique: Simplifies nested IO structures
   */
  def testFlatten: IO[Unit] = {
    println("\nTesting flatten...")

    val nested: IO[IO[String]] = IO.pure(IO.pure("Nested value"))
    val flattened: IO[String] = nested.flatten

    for {
      result <- flattened
      _ <- IO.println(s"  Flattened result: $result")
    } yield ()
  }

  // =====================================
  // 3. ERROR HANDLING
  // =====================================

  /**
   * attempt - Converts errors to Either
   * Unique: Reifies errors as values for pattern matching
   */
  def testAttempt: IO[Unit] = {
    println("\nTesting attempt...")

    val risky = IO.raiseError[Int](new Exception("Error"))
    val attempted: IO[Either[Throwable, Int]] = risky.attempt

    for {
      either <- attempted
      _ <- either match {
        case Left(error) => IO.println(s"  Got error: ${error.getMessage}")
        case Right(value) => IO.println(s"  Got value: $value")
      }
    } yield ()
  }

  /**
   * handleErrorWith - Recovers from errors with another IO
   * Unique: Allows error-dependent recovery strategies
   */
  def testHandleErrorWith: IO[Unit] = {
    println("\nTesting handleErrorWith...")

    val failingIO = IO.raiseError[Int](new IllegalArgumentException("Bad input"))

    val recovered = failingIO.handleErrorWith {
      case _: IllegalArgumentException =>
        IO.println("  Handling IllegalArgumentException") >> IO.pure(42)
      case other =>
        IO.raiseError(other)
    }

    recovered.void
  }

  /**
   * redeem - Handles both success and error cases
   * Unique: Transforms both error and success paths in one operation
   */
  def testRedeem: IO[Unit] = {
    println("\nTesting redeem...")

    val risky = if (Random.nextBoolean())
      IO.pure(100)
    else
      IO.raiseError[Int](new Exception("Random failure"))

    val redeemed = risky.redeem(
      error => s"Failed: ${error.getMessage}",
      value => s"Success: $value"
    )

    for {
      result <- redeemed
      _ <- IO.println(s"  Redeemed result: $result")
    } yield ()
  }

  // =====================================
  // 4. RESOURCE MANAGEMENT
  // =====================================

  /**
   * bracket - Ensures resource cleanup
   * Unique: Guarantees finalizer runs even on errors/cancellation
   */
  def testBracket: IO[Unit] = {
    println("\nTesting bracket...")

    val resource = (
      IO.println("  Acquiring resource") >> IO.pure("Resource")
    ).bracket(resource =>
      IO.println(s"  Using $resource")
    )(resource =>
      IO.println(s"  Releasing $resource")
    )

    resource
  }

  /**
   * guarantee - Ensures finalizer always runs
   * Unique: Like finally block but for IO
   */
  def testGuarantee: IO[Unit] = {
    println("\nTesting guarantee...")

    val action = IO.println("  Main action")
    val cleanup = IO.println("  Cleanup always runs")

    val guaranteed = action.guarantee(cleanup)
    guaranteed
  }

  /**
   * onCancel - Registers cancellation handler
   * Unique: Specific cleanup for cancellation scenarios
   */
  def testOnCancel: IO[Unit] = {
    println("\nTesting onCancel...")

    val cancellable = IO.sleep(1.second)
      .onCancel(IO.println("  Was cancelled!"))

    for {
      fiber <- cancellable.start
      _ <- IO.sleep(100.millis)
      _ <- fiber.cancel
      _ <- IO.sleep(100.millis)
    } yield ()
  }

  // =====================================
  // 5. CONCURRENCY
  // =====================================

  /**
   * start - Spawns a fiber (lightweight thread)
   * Unique: Enables true concurrent execution
   */
  def testStart: IO[Unit] = {
    println("\nTesting start...")

    val task = IO.sleep(200.millis) >> IO.println("  Fiber completed")

    for {
      fiber <- task.start
      _ <- IO.println("  Main thread continues")
      _ <- fiber.join // Wait for fiber to complete
    } yield ()
  }

  /**
   * race - Runs two IOs concurrently, returns first to complete
   * Unique: Automatic cancellation of the loser
   */
  def testRace: IO[Unit] = {
    println("\nTesting race...")

    val fast = IO.sleep(100.millis) >> IO.pure("Fast")
    val slow = IO.sleep(200.millis) >> IO.pure("Slow")

    for {
      winner <- IO.race(fast, slow)
      _ <- IO.println(s"  Winner: $winner")
    } yield ()
  }

  /**
   * both - Runs two IOs concurrently, waits for both
   * Unique: Parallel execution with result aggregation
   */
  def testBoth: IO[Unit] = {
    println("\nTesting both...")

    val task1 = IO.sleep(100.millis) >> IO.pure(1)
    val task2 = IO.sleep(150.millis) >> IO.pure(2)

    for {
      (result1, result2) <- task1.both(task2)
      _ <- IO.println(s"  Both results: $result1, $result2")
    } yield ()
  }

  /**
   * parMapN - Parallel execution with result combination
   * Unique: Applicative parallel composition
   */
  def testParMapN: IO[Unit] = {
    println("\nTesting parMapN...")

    val io1 = IO.sleep(100.millis) >> IO.pure(1)
    val io2 = IO.sleep(100.millis) >> IO.pure(2)
    val io3 = IO.sleep(100.millis) >> IO.pure(3)

    val combined = (io1, io2, io3).parMapN { (a, b, c) =>
      a + b + c
    }

    for {
      result <- combined
      _ <- IO.println(s"  ParMapN result: $result")
    } yield ()
  }

  // =====================================
  // 6. TIMING OPERATIONS
  // =====================================

  /**
   * sleep - Non-blocking delay
   * Unique: Suspends fiber without blocking thread
   */
  def testSleep: IO[Unit] = {
    println("\nTesting sleep...")

    for {
      _ <- IO.println("  Before sleep")
      _ <- IO.sleep(100.millis)
      _ <- IO.println("  After sleep")
    } yield ()
  }

  /**
   * timeout - Fails if IO doesn't complete in time
   * Unique: Time-bounds operations with automatic cancellation
   */
  def testTimeout: IO[Unit] = {
    println("\nTesting timeout...")

    val slow = IO.sleep(1.second) >> IO.pure("Completed")

    val timeBounded = slow.timeout(100.millis).attempt

    for {
      result <- timeBounded
      _ <- result match {
        case Left(_) => IO.println("  Operation timed out")
        case Right(value) => IO.println(s"  Completed: $value")
      }
    } yield ()
  }

  // =====================================
  // 7. EXECUTION CONTROL
  // =====================================

  /**
   * uncancelable - Prevents cancellation in critical sections
   * Unique: Ensures atomic operations complete
   */
  def testUncancelable: IO[Unit] = {
    println("\nTesting uncancelable...")

    val critical = IO.uncancelable { poll =>
      IO.println("  Starting critical section") >>
        IO.sleep(100.millis) >>
        IO.println("  Critical section done")
    }

    critical
  }

  /**
   * interruptible - Makes blocking code cancellable
   * Unique: Bridges blocking operations with IO's cancellation
   */
  def testInterruptible: IO[Unit] = {
    println("\nTesting interruptible...")

    val blockingButCancellable = IO.interruptible {
      Thread.sleep(50)
      println("  Interruptible blocking completed")
      "Result"
    }

    blockingButCancellable.void
  }

  // =====================================
  // 8. DEBUGGING METHODS
  // =====================================

  /**
   * debug - Prints value with thread info
   * Unique: Built-in debugging with context
   */
  def testDebug: IO[Unit] = {
    println("\nTesting debug...")

    val io = IO.pure(42).debug()
    io.void
  }

  // =====================================
  // 9. SEQUENCING OPERATORS
  // =====================================

  /**
   * >> and *> - Sequence operations, discard left result
   * Unique: >> is lazy, *> is eager evaluation
   */
  def testSequencing: IO[Unit] = {
    println("\nTesting sequencing operators...")

    val first = IO.println("  First")
    val second = IO.println("  Second")

    // >> is lazy - second only created if first succeeds
    val lazySeq = first >> second

    // *> is eager - both effects are evaluated
    val eagerSeq = first *> second

    for {
      _ <- IO.println("  Using >>")
      _ <- lazySeq
      _ <- IO.println("  Using *>")
      _ <- eagerSeq
    } yield ()
  }

  /**
   * &> and <& - Parallel sequencing operators
   * Unique: Run in parallel but return specific result
   */
  def testParallelSequencing: IO[Unit] = {
    println("\nTesting parallel sequencing...")

    val task1 = IO.sleep(100.millis) >> IO.pure(1)
    val task2 = IO.sleep(100.millis) >> IO.pure(2)

    for {
      result1 <- task1 &> task2  // Run both, return task2's result
      _ <- IO.println(s"  &> result: $result1")
      result2 <- task1 <& task2  // Run both, return task1's result
      _ <- IO.println(s"  <& result: $result2")
    } yield ()
  }

  // =====================================
  // 10. MEMOIZATION
  // =====================================

  /**
   * memoize - Caches the result of first execution
   * Unique: Share computation results across fibers
   */
  def testMemoize: IO[Unit] = {
    println("\nTesting memoize...")

    val expensive = IO {
      println("  Computing expensive value...")
      Thread.sleep(100)
      Random.nextInt(100)
    }

    for {
      memoized <- expensive.memoize
      result1 <- memoized
      result2 <- memoized
      result3 <- memoized
      _ <- IO.println(s"  All results same: $result1, $result2, $result3")
    } yield ()
  }

  // =====================================
  // 11. ADVANCED METHODS
  // =====================================

  /**
   * fromFuture - Converts Scala Future to IO
   * Unique: Bridges Future-based APIs to IO
   */
  def testFromFuture: IO[Unit] = {
    println("\nTesting fromFuture...")

    val futureIO = IO.fromFuture(IO {
      Future.successful("From Future")
    })

    for {
      result <- futureIO
      _ <- IO.println(s"  Future result: $result")
    } yield ()
  }

  /**
   * evalOn - Runs IO on specific ExecutionContext
   * Unique: Control thread pool for specific operations
   */
  def testEvalOn: IO[Unit] = {
    println("\nTesting evalOn...")

    val ec = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())

    val onSpecificEC = IO {
      println(s"  Running on: ${Thread.currentThread().getName}")
    }.evalOn(ec)

    onSpecificEC
  }

  /**
   * ref - Creates a mutable reference
   * Unique: Thread-safe mutable state in pure FP
   */
  def testRef: IO[Unit] = {
    println("\nTesting Ref...")

    for {
      ref <- IO.ref(0)
      _ <- ref.update(_ + 1)
      _ <- ref.update(_ + 1)
      value <- ref.get
      _ <- IO.println(s"  Ref value: $value")
    } yield ()
  }

  /**
   * deferred - Creates a promise-like structure
   * Unique: One-time settable value for coordination
   */
  def testDeferred: IO[Unit] = {
    println("\nTesting Deferred...")

    for {
      deferred <- IO.deferred[String]
      fiber <- deferred.get.start // Start waiting for value
      _ <- IO.sleep(100.millis)
      _ <- deferred.complete("Completed value")
      result <- fiber.joinWithNever
      _ <- IO.println(s"  Deferred result: $result")
    } yield ()
  }

  // Main execution
  def run: IO[Unit] = {
    for {
      // Creation methods
      _ <- testApplyDelay
      _ <- testPure
      _ <- testRaiseError
      _ <- testAsync
      _ <- testBlocking
      _ <- testDefer
      _ <- testNever

      // Transformation methods
      _ <- testMap
      _ <- testFlatMap
      _ <- testFlatten

      // Error handling
      _ <- testAttempt
      _ <- testHandleErrorWith
      _ <- testRedeem

      // Resource management
      _ <- testBracket
      _ <- testGuarantee
      _ <- testOnCancel

      // Concurrency
      _ <- testStart
      _ <- testRace
      _ <- testBoth
      _ <- testParMapN

      // Timing
      _ <- testSleep
      _ <- testTimeout

      // Execution control
      _ <- testUncancelable
      _ <- testInterruptible

      // Debugging
      _ <- testDebug

      // Sequencing
      _ <- testSequencing
      _ <- testParallelSequencing

      // Memoization
      _ <- testMemoize

      // Advanced
      _ <- testFromFuture
      _ <- testEvalOn
      _ <- testRef
      _ <- testDeferred

      _ <- IO.println("\n=== All tests completed ===")
    } yield ()
  }
}