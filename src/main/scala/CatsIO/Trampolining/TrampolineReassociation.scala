package CatsIO.Trampolining

object TrampolineReassociation {
  // STEP 5: Deep dive into reassociation - the critical optimization
  enum Trampoline[+A]:
    case Done(value: A)
    case More(thunk: () => Trampoline[A])
    case FlatMap[A, B](
                        source: Trampoline[A],
                        continuation: A => Trampoline[B]
                      ) extends Trampoline[B]

    def flatMap[B](f: A => Trampoline[B]): Trampoline[B] =
      FlatMap(this, f)

    // Version WITHOUT reassociation (will overflow)
    def runNaive: A =
      this match
        case Done(value) => value
        case More(thunk) => thunk().runNaive
        case FlatMap(source, cont) =>
          // Problem: source.runNaive is NOT in tail position!
          val a = source.runNaive // Recursive call
          cont(a).runNaive // Another recursive call

    // Version WITH reassociation (stack-safe)
    @annotation.tailrec
    final def run: A =
      this match
        case Done(value) => value

        case More(thunk) =>
          thunk().run

        case FlatMap(source, cont) =>
          source match
            case Done(value) =>
              cont(value).run

            case More(thunk) =>
              thunk().flatMap(cont).run

            // CRITICAL: Reassociate nested FlatMaps
            case FlatMap(innerSource, innerCont) =>
              // Transform: FlatMap(FlatMap(a, f), g)
              // Into: FlatMap(a, x => FlatMap(f(x), g))
              innerSource.flatMap(x =>
                innerCont(x).flatMap(cont)
              ).run

  object Trampoline:
    def pure[A](value: A): Trampoline[A] = Done(value)

    def defer[A](thunk: => Trampoline[A]): Trampoline[A] = More(() => thunk)

  @main def step5Demo(): Unit =
    println("=== Step 5: The Reassociation Magic ===\n")

    println("--- The Problem: Left-Nested Structure ---")
    println("When you write:")
    println("  pure(1).flatMap(f).flatMap(g).flatMap(h)")
    println("\nYou get:")
    println("  FlatMap(FlatMap(FlatMap(Done(1), f), g), h)")
    println()
    println("This is LEFT-NESTED (left-associative):")
    println("  ((Done(1).flatMap(f)).flatMap(g)).flatMap(h)")
    println()
    println("ASCII art:")
    println("        FlatMap(h)")
    println("           /")
    println("      FlatMap(g)")
    println("         /")
    println("    FlatMap(f)")
    println("       /")
    println("    Done(1)")
    println()
    println("To interpret this, we must traverse DOWN the left side")
    println("This creates stack frames!\n")

    println("--- The Solution: Right-Reassociation ---")
    println("We transform it to:")
    println("  FlatMap(Done(1), x => FlatMap(f(x), y => FlatMap(g(y), h)))")
    println()
    println("This is RIGHT-NESTED:")
    println("  Done(1).flatMap(x => f(x).flatMap(y => g(y).flatMap(h)))")
    println()
    println("ASCII art:")
    println("    FlatMap")
    println("    /     \\")
    println("  Done(1) (x => FlatMap)")
    println("                /     \\")
    println("              f(x)   (y => FlatMap)")
    println("                          /     \\")
    println("                        g(y)    h")
    println()
    println("Now we can process top-down in tail position!\n")

    println("--- Step-by-Step Reassociation Example ---")

    // Let's trace it manually
    def f(x: Int): Trampoline[Int] = {
      println(s"    f($x) = ${x + 1}")
      Trampoline.Done(x + 1)
    }

    def g(x: Int): Trampoline[Int] = {
      println(s"    g($x) = ${x * 2}")
      Trampoline.Done(x * 2)
    }

    def h(x: Int): Trampoline[Int] = {
      println(s"    h($x) = ${x - 1}")
      Trampoline.Done(x - 1)
    }

    val computation =
      Trampoline.Done(5)
        .flatMap(f)
        .flatMap(g)
        .flatMap(h)

    println("Initial structure: FlatMap(FlatMap(FlatMap(Done(5), f), g), h)")
    println("\nExecution trace:")
    println("Step 1: Match FlatMap(source=FlatMap(FlatMap(Done(5), f), g), cont=h)")
    println("        source is also a FlatMap, so REASSOCIATE")
    println()
    println("Step 2: Extract inner parts:")
    println("        innerSource = FlatMap(Done(5), f)")
    println("        innerCont = g")
    println("        outerCont = h")
    println()
    println("Step 3: Create new structure:")
    println("        FlatMap(innerSource, x => innerCont(x).flatMap(outerCont))")
    println("        = FlatMap(FlatMap(Done(5), f), x => g(x).flatMap(h))")
    println()
    println("Step 4: Run again - match FlatMap with source=FlatMap(Done(5), f)")
    println("        Reassociate again!")
    println()
    println("Step 5: Now we have:")
    println("        FlatMap(Done(5), x => f(x).flatMap(y => g(y).flatMap(h)))")
    println()
    println("Step 6: source is Done(5), so execute the chain:")

    val result = computation.run
    println(s"\nFinal result: $result")
    println("Expected: h(g(f(5))) = h(g(6)) = h(12) = 11\n")

    println("--- Why This Works ---")
    println("1. Each reassociation moves us closer to Done")
    println("2. We compose continuations: x => f(x).flatMap(g)")
    println("3. Continuations are stored as lambda objects on the heap")
    println("4. No stack depth accumulation!")
    println()
    println("Left-nested:  Must traverse left side recursively")
    println("Right-nested: Process top-to-bottom in tail position\n")

    println("--- Visualization of Stack Behavior ---")

    println("\nWITHOUT reassociation (runNaive):")
    println("run FlatMap(FlatMap(FlatMap(Done, f), g), h)")
    println("  ↓ [Stack Frame 1]")
    println("  run FlatMap(FlatMap(Done, f), g)")
    println("    ↓ [Stack Frame 2]")
    println("    run FlatMap(Done, f)")
    println("      ↓ [Stack Frame 3]")
    println("      run Done = value")
    println("      ← apply f")
    println("    ← apply g")
    println("  ← apply h")
    println("Stack depth = chain depth = O(n)\n")

    println("WITH reassociation (run):")
    println("run FlatMap(FlatMap(FlatMap(Done, f), g), h)")
    println("  ↓ reassociate [Same Frame]")
    println("run FlatMap(FlatMap(Done, f), x => g(x).flatMap(h))")
    println("  ↓ reassociate [Same Frame]")
    println("run FlatMap(Done, x => f(x).flatMap(y => g(y).flatMap(h)))")
    println("  ↓ Done case [Same Frame]")
    println("run f(value).flatMap(y => g(y).flatMap(h))")
    println("  ↓ [Same Frame]")
    println("run g(value2).flatMap(h)")
    println("  ↓ [Same Frame]")
    println("run h(value3)")
    println("Stack depth = O(1) always!")

    println("\n--- The Mathematical Property ---")
    println("Reassociation is based on the associativity of flatMap:")
    println("  (m.flatMap(f)).flatMap(g) == m.flatMap(x => f(x).flatMap(g))")
    println()
    println("We're not changing the computation, just its representation!")
}
