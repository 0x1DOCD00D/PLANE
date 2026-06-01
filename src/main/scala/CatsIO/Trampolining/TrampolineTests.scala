package CatsIO.Trampolining

object TrampolineTests {
  // STEP 6: Practical examples showing stack-safety in action

  enum Trampoline[+A]:
    case Done(value: A)
    case More(thunk: () => Trampoline[A])
    case FlatMap[A, B](
                        source: Trampoline[A],
                        continuation: A => Trampoline[B]
                      ) extends Trampoline[B]

    def flatMap[B](f: A => Trampoline[B]): Trampoline[B] =
      FlatMap(this, f)

    def map[B](f: A => B): Trampoline[B] =
      flatMap(a => Done(f(a)))

    @annotation.tailrec
    final def run: A =
      this match
        case Done(value) => value
        case More(thunk) => thunk().run
        case FlatMap(source, cont) =>
          source match
            case Done(value) => cont(value).run
            case More(thunk) => thunk().flatMap(cont).run
            case FlatMap(innerSource, innerCont) =>
              innerSource.flatMap(x => innerCont(x).flatMap(cont)).run

  object Trampoline:
    def pure[A](value: A): Trampoline[A] = Done(value)

    def defer[A](thunk: => Trampoline[A]): Trampoline[A] = More(() => thunk)

  @main def step6Demo(): Unit =
    println("=== Step 6: Practical Examples ===\n")

    // Example 1: Countdown (tail recursive pattern)
    println("--- Example 1: Countdown ---")

    def countdown(n: Int): Trampoline[Int] =
      if n <= 0 then
        Trampoline.pure(0)
      else
        Trampoline.defer(countdown(n - 1)) // Suspend recursion

    println("Countdown from 5:")
    println(s"Result: ${countdown(5).run}")

    println("\nCountdown from 100,000 (would overflow without trampolining):")
    val start1 = System.nanoTime()
    val result1 = countdown(100000).run
    val time1 = (System.nanoTime() - start1) / 1_000_000
    println(s"Result: $result1")
    println(s"Time: ${time1}ms")
    println("✓ No stack overflow!\n")

    // Example 2: Sum of list
    println("--- Example 2: Sum List (shows accumulator pattern) ---")

    def sumList(list: List[Int]): Trampoline[Int] =
      def go(remaining: List[Int], acc: Int): Trampoline[Int] =
        remaining match
          case Nil =>
            Trampoline.pure(acc)
          case head :: tail =>
            Trampoline.defer(go(tail, acc + head))

      go(list, 0)

    val smallList = List(1, 2, 3, 4, 5)
    println(s"Sum of $smallList: ${sumList(smallList).run}")

    val largeList = (1 to 50000).toList
    val start2 = System.nanoTime()
    val sum = sumList(largeList).run
    val time2 = (System.nanoTime() - start2) / 1_000_000
    println(s"Sum of 1 to 50,000: $sum")
    println(s"Time: ${time2}ms")
    println("✓ No stack overflow!\n")

    // Example 3: FlatMap chains (monadic pattern)
    println("--- Example 3: Deep FlatMap Chains ---")

    def deepFlatMapChain(n: Int): Trampoline[Int] =
      if n <= 0 then
        Trampoline.pure(0)
      else
        Trampoline.pure(n).flatMap(x =>
          Trampoline.defer(deepFlatMapChain(x - 1))
            .flatMap(result => Trampoline.pure(x + result))
        )

    println("Chain depth 5:")
    println(s"Result: ${deepFlatMapChain(5).run}")

    println("\nChain depth 10,000:")
    val start3 = System.nanoTime()
    val result3 = deepFlatMapChain(10000).run
    val time3 = (System.nanoTime() - start3) / 1_000_000
    println(s"Result: $result3")
    println(s"Time: ${time3}ms")
    println("✓ No stack overflow!\n")

    // Example 4: Fibonacci
    println("--- Example 4: Fibonacci (performance comparison) ---")

    def fib(n: Int): Trampoline[BigInt] =
      def go(n: Int, a: BigInt, b: BigInt): Trampoline[BigInt] =
        if n == 0 then
          Trampoline.pure(a)
        else
          Trampoline.defer(go(n - 1, b, a + b))

      go(n, 0, 1)

    println("Fibonacci(100):")
    val fib100 = fib(100).run
    println(s"Result: $fib100")

    println("\nFibonacci(10,000):")
    val start4 = System.nanoTime()
    val fib10k = fib(10000).run
    val time4 = (System.nanoTime() - start4) / 1_000_000
    println(s"Result length: ${fib10k.toString.length} digits")
    println(s"Time: ${time4}ms")
    println("✓ No stack overflow!\n")

    // Example 5: Map over list (practical use case)
    println("--- Example 5: Map Over Large List ---")

    def mapList[A, B](list: List[A])(f: A => B): Trampoline[List[B]] =
      list match
        case Nil =>
          Trampoline.pure(Nil)
        case head :: tail =>
          Trampoline.defer(mapList(tail)(f))
            .flatMap(mappedTail => Trampoline.pure(f(head) :: mappedTail))

    val numbers = (1 to 30000).toList
    val start5 = System.nanoTime()
    val squared = mapList(numbers)(x => x * x).run
    val time5 = (System.nanoTime() - start5) / 1_000_000
    println(s"Mapped ${numbers.length} elements")
    println(s"First 5 results: ${squared.take(5)}")
    println(s"Time: ${time5}ms")
    println("✓ No stack overflow!\n")

    // Example 6: Nested flatMap chains (stress test)
    println("--- Example 6: Heavily Nested FlatMaps ---")

    def nestedChains(depth: Int): Trampoline[Int] =
      if depth <= 0 then
        Trampoline.pure(1)
      else
        Trampoline.pure(depth)
          .flatMap(x => Trampoline.pure(x))
          .flatMap(x => Trampoline.pure(x))
          .flatMap(x => Trampoline.pure(x))
          .flatMap(_ => Trampoline.defer(nestedChains(depth - 1)))

    println("Creating deeply nested structure with 5,000 levels...")
    val start6 = System.nanoTime()
    val result6 = nestedChains(5000).run
    val time6 = (System.nanoTime() - start6) / 1_000_000
    println(s"Result: $result6")
    println(s"Time: ${time6}ms")
    println("✓ No stack overflow!\n")

    // Summary
    println("--- Performance Summary ---")
    println(f"Countdown 100k:     ${time1}%5dms")
    println(f"Sum 50k elements:   ${time2}%5dms")
    println(f"FlatMap chain 10k:  ${time3}%5dms")
    println(f"Fibonacci 10k:      ${time4}%5dms")
    println(f"Map 30k elements:   ${time5}%5dms")
    println(f"Nested chains 5k:   ${time6}%5dms")
    println("\n✓ All operations completed without stack overflow!")
    println("✓ Heap usage instead of stack")
    println("✓ O(1) stack depth regardless of recursion depth")
}
