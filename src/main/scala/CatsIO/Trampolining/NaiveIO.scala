package CatsIO.Trampolining

// STEP 1: Understanding why naive recursion fails

// Naive approach - THIS WILL OVERFLOW
class NaiveIO[+A](private val effect: () => A):
  def flatMap[B](f: A => NaiveIO[B]): NaiveIO[B] =
    new NaiveIO(() => f(effect()).run())
//    f(effect())

  def run(): A = effect()

object NaiveIO:
  def pure[A](value: A): NaiveIO[A] =
    new NaiveIO(() => value)

// Example showing the problem
def naiveCountdown(n: Int): NaiveIO[Int] =
  if n <= 0 then NaiveIO.pure(0)
  else NaiveIO.pure(n).flatMap(_ => naiveCountdown(n - 1))

@main def step1Demo(): Unit =
  println("=== Step 1: Understanding Stack Overflow ===\n")

  // Visualizing the problem
  println("What happens with naive flatMap:")
  println("1. countdown(3).run()")
  println("2. -> pure(3).flatMap(_ => countdown(2)).run()")
  println("3. -> countdown(2).run()  [Stack frame 1]")
  println("4. -> pure(2).flatMap(_ => countdown(1)).run()")
  println("5. -> countdown(1).run()  [Stack frame 2]")
  println("6. -> pure(1).flatMap(_ => countdown(0)).run()")
  println("7. -> countdown(0).run()  [Stack frame 3]")
  println("8. -> pure(0).run()")
  println("9. Each flatMap adds a frame to the call stack!\n")

  // This works for small numbers
  println(s"Small countdown (10): ${naiveCountdown(10).run()}")

  // Uncomment to see stack overflow (don't actually run this!)
  // println(s"Large countdown (100000): ${naiveCountdown(100000).run()}")
  println("\nLarge countdown (100000): Would cause StackOverflowError!")

  println("\n--- Why This Happens ---")
  println("Each flatMap call creates a NEW function that:")
  println("1. Runs the previous effect: effect()")
  println("2. Passes result to f")
  println("3. Runs the result: f(result).run()")
  println("\nThis creates nested function calls:")
  println("run() -> run() -> run() -> ... (on the stack)")

  println("\n--- Stack Visualization ---")
  def showStackDepth(n: Int, depth: Int = 0): NaiveIO[Unit] =
    if n <= 0 then
      NaiveIO.pure {
        println(s"${"  " * depth}[Depth $depth] Reached bottom!")
      }
    else
      NaiveIO.pure {
        println(s"${"  " * depth}[Depth $depth] n=$n")
      }.flatMap(_ => showStackDepth(n - 1, depth + 1))

  println("\nShowing stack depth for countdown(5):")
  showStackDepth(5).run()
  println("\nImagine this with 100,000 levels!")