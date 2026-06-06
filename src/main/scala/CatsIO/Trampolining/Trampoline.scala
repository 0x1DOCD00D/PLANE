package CatsIO.Trampolining

// STEP 3: Building the Trampoline type step by step

// Version 1: Just values
enum TrampolineV1[+A]:
  case Done(value: A)

// Version 2: Add suspension
enum TrampolineV2[+A]:
  case Done(value: A)
  case More(thunk: () => TrampolineV2[A])

// Version 3: Add flatMap structure (FULL VERSION)
enum Trampoline[+A]:
  case Done(value: A)
  case More(thunk: () => Trampoline[A])
  case FlatMap[A, B](
                      source: Trampoline[A],
                      continuation: A => Trampoline[B]
                    ) extends Trampoline[B]

  // flatMap doesn't execute - it creates data!
  def flatMap[B](f: A => Trampoline[B]): Trampoline[B] =
    FlatMap(this, f)

  def map[B](f: A => B): Trampoline[B] =
    flatMap(a => Done(f(a)))

object Trampoline:
  def pure[A](value: A): Trampoline[A] = Done(value)

  def defer[A](thunk: => Trampoline[A]): Trampoline[A] =
    More(() => thunk)

@main def step3Demo(): Unit =
  println("=== Step 3: Building the Trampoline Type ===\n")

  // Show what flatMap creates
  println("--- What flatMap Actually Does ---")
  val step1 = Trampoline.pure(5)
  println(s"Step 1 - pure(5): ${step1.getClass.getSimpleName}")

  val step2 = step1.flatMap(x => Trampoline.pure(x * 2))
  println(s"Step 2 - flatMap(...): ${step2.getClass.getSimpleName}")
  println("Created a FlatMap object, didn't execute anything!")

  val step3 = step2.flatMap(x => Trampoline.pure(x + 10))
  println(s"Step 3 - another flatMap: ${step3.getClass.getSimpleName}")
  println("Created another FlatMap wrapping the previous one!\n")

  // Visualize the structure
  println("--- Structure Visualization ---")
  println("step3 in memory:")
  println("FlatMap(")
  println("  source = FlatMap(")
  println("    source = Done(5),")
  println("    continuation = x => Done(x * 2)")
  println("  ),")
  println("  continuation = x => Done(x + 10)")
  println(")")
  println("\nThis is a TREE STRUCTURE in heap memory!\n")

  // Show defer in action
  println("--- Understanding defer ---")
  var deferExecuted = false
  val deferred = Trampoline.defer {
    deferExecuted = true
    println("  [Inside defer thunk]")
    Trampoline.pure(100)
  }
  println(s"After creating defer, executed = $deferExecuted")
  println("The thunk is suspended in a More case class!\n")

  // Complex example
  println("--- Complex Chain Example ---")
  val complex =
    Trampoline.pure(1)
      .flatMap(x => {
        println(s"  [Defining flatMap 1: x=$x]")
        Trampoline.pure(x + 1)
      })
      .flatMap(x => {
        println(s"  [Defining flatMap 2: x=$x]")
        Trampoline.pure(x * 2)
      })
      .flatMap(x => {
        println(s"  [Defining flatMap 3: x=$x]")
        Trampoline.pure(x - 1)
      })

  println("\nNotice: The println statements INSIDE flatMap didn't execute!")
  println("We only executed the code OUTSIDE the lambdas.")
  println(s"Result type: ${complex.getClass.getSimpleName}\n")

  // Pattern matching on structure
  println("--- Pattern Matching the Structure ---")
  def describe[A](t: Trampoline[A], indent: Int = 0): Unit =
    val prefix = "  " * indent
    t match
      case Trampoline.Done(v) =>
        println(s"${prefix}Done($v)")

      case Trampoline.More(thunk) =>
        println(s"${prefix}More(<thunk>)")

      case Trampoline.FlatMap(source, cont) =>
        println(s"${prefix}FlatMap(")
        describe(source, indent + 1)
        println(s"$prefix  <continuation>")
        println(s"$prefix)")

  val example = Trampoline.pure(10)
    .flatMap(x => Trampoline.pure(x + 5))

  println("Structure of: pure(10).flatMap(x => pure(x + 5))")
  describe(example)

  println("\n--- Key Takeaways ---")
  println("1. flatMap creates a FlatMap case class (data)")
  println("2. The continuation function is STORED, not executed")
  println("3. We build a tree structure in heap memory")
  println("4. No recursion happens yet - just data construction")
  println("5. This is why we can build arbitrarily deep chains!")