package CatsIO.Trampolining

// STEP 2: Converting computation into data structures

// Key insight: Instead of executing immediately, CREATE A DESCRIPTION
enum Computation[+A]:
  case Value(a: A)                                    // A finished value
  case Suspend(thunk: () => A)                        // Suspended computation
  case Chain[A, B](                                   // Two-step computation
                                                      source: Computation[A],
                                                      next: A => Computation[B]
                  ) extends Computation[B]

@main def step2Demo(): Unit =
  println("=== Step 2: Computation as Data ===\n")

  // Example 1: Simple values as data
  println("--- Example 1: Simple Values ---")
  val simpleValue: Computation[Int] = Computation.Value(42)
  println(s"Created data: $simpleValue")
  println("Notice: We created a data structure, not executed anything!\n")

  // Example 2: Suspended computation
  println("--- Example 2: Suspended Computation ---")
  val suspended: Computation[Int] = Computation.Suspend(() => {
    println("  [This runs when we interpret it]")
    10 + 20
  })
  println(s"Created suspended computation: ${suspended.getClass.getSimpleName}")
  println("The lambda hasn't executed yet - it's just data!\n")

  // Example 3: Chained computations
  println("--- Example 3: Chained Computation ---")
  val chained: Computation[String] =
    Computation.Chain(
      Computation.Value(5),
      (n: Int) => Computation.Value(s"Result: $n")
    )
  println(s"Created chain: $chained")
  println("Again, nothing executed - just a data structure!\n")

  // Example 4: Nested chains
  println("--- Example 4: Nested Chains (The Key Idea) ---")
  val nested: Computation[Int] =
    Computation.Chain(
      Computation.Value(1),
      (x: Int) => Computation.Chain(
        Computation.Value(x + 1),
        (y: Int) => Computation.Chain(
          Computation.Value(y + 1),
          (z: Int) => Computation.Value(z + 1)
        )
      )
    )

  println("Created a nested chain structure:")
  println("Chain(")
  println("  Value(1),")
  println("  x => Chain(")
  println("    Value(x+1),")
  println("    y => Chain(")
  println("      Value(y+1),")
  println("      z => Value(z+1)")
  println("    )")
  println("  )")
  println(")")
  println("\nThis is a TREE in memory, not a stack of function calls!")

  // Example 5: Building countdown as data
  println("\n--- Example 5: Countdown as Data Structure ---")
  def countdownAsData(n: Int): Computation[Int] =
    if n <= 0 then
      Computation.Value(0)
    else
      Computation.Chain(
        Computation.Value(n),
        (_: Int) => countdownAsData(n - 1)
      )

  val countdown3 = countdownAsData(3)
  println("countdownAsData(3) creates:")
  println("Chain(Value(3), _ => ")
  println("  Chain(Value(2), _ => ")
  println("    Chain(Value(1), _ => ")
  println("      Value(0))")
  println("    )")
  println("  )")
  println(")")

  println("\n--- The Critical Difference ---")
  println("BEFORE (Naive): Recursion happens during EXECUTION")
  println("  - Each call adds to the stack")
  println("  - Stack depth = recursion depth")
  println()
  println("AFTER (Data): Recursion happens during DATA CONSTRUCTION")
  println("  - Creates objects on the heap")
  println("  - Stack depth = O(1) during construction")
  println("  - We can interpret the tree iteratively!")

  println("\n--- Memory Representation ---")
  println("Naive approach uses STACK:")
  println("  [countdown(3)] -> [countdown(2)] -> [countdown(1)] -> [countdown(0)]")
  println("  └─ Stack Frame 1  └─ Stack Frame 2  └─ Stack Frame 3")
  println()
  println("Data approach uses HEAP:")
  println("  Chain object -> Chain object -> Chain object -> Value object")
  println("  └─ Heap         └─ Heap         └─ Heap         └─ Heap")