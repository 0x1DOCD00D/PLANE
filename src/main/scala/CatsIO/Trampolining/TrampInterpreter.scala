package CatsIO.Trampolining

// STEP 4: Understanding the iterative interpreter

enum TrampolineStruct[+A]:
  case Done(value: A)
  case More(thunk: () => TrampolineStruct[A])
  case FlatMap[A, B](
                      source: TrampolineStruct[A],
                      continuation: A => TrampolineStruct[B]
                    ) extends TrampolineStruct[B]

  def flatMap[B](f: A => TrampolineStruct[B]): TrampolineStruct[B] =
    FlatMap(this, f)

  // Simple (inefficient) interpreter - just to show the concept
  def runSimple: A =
    this match
      case Done(value) =>
        value

      case More(thunk) =>
        thunk().runSimple

      case FlatMap(source, cont) =>
        val result = source.runSimple
        cont(result).runSimple

  // Optimized tail-recursive interpreter (the real one)
  @annotation.tailrec
  final def run: A =
    this match
      case Done(value) =>
        value

      case More(thunk) =>
        thunk().run  // Tail call

      case FlatMap(source, cont) =>
        source match
          case Done(value) =>
            cont(value).run  // Tail call

          case More(thunk) =>
            thunk().flatMap(cont).run  // Tail call

          // THE MAGIC: Reassociation
          case FlatMap(innerSource, innerCont) =>
            innerSource.flatMap(x =>
              innerCont(x).flatMap(cont)
            ).run  // Tail call

object TrampolineStruct:
  def pure[A](value: A): TrampolineStruct[A] = Done(value)
  def defer[A](thunk: => TrampolineStruct[A]): TrampolineStruct[A] = More(() => thunk)

@main def step4Demo(): Unit =
  println("=== Step 4: The Iterative Interpreter ===\n")

  // Example 1: Simple interpretation
  println("--- Example 1: Interpreting Done ---")
  val simple = TrampolineStruct.Done(42)
  println(s"Structure: $simple")
  println(s"Pattern matches: Done(42)")
  println(s"Returns: 42")
  println(s"Result: ${simple.run}\n")

  // Example 2: Interpreting More
  println("--- Example 2: Interpreting More ---")
  var stepCount = 0
  val withMore = TrampolineStruct.More(() => {
    stepCount += 1
    println(s"  [Step $stepCount: Evaluating thunk]")
    TrampolineStruct.Done(100)
  })
  println("Before run: thunk not executed")
  println(s"Result: ${withMore.run}")
  println(s"After run: thunk executed $stepCount time(s)\n")

  // Example 3: Interpreting FlatMap
  println("--- Example 3: Interpreting Simple FlatMap ---")
  val simpleFlatMap =
    TrampolineStruct.Done(5).flatMap(x => {
      println(s"  [Inside continuation with x=$x]")
      TrampolineStruct.Done(x * 2)
    })

  println("Structure:")
  println("FlatMap(Done(5), x => Done(x * 2))")
  println("\nExecution steps:")
  println("1. Match FlatMap(Done(5), cont)")
  println("2. Source is Done(5), so call cont(5)")
  println("3. Execute continuation -> Done(10)")
  println("4. Match Done(10)")
  println("5. Return 10")
  println(s"\nRunning: ${simpleFlatMap.run}\n")

  // Example 4: Nested FlatMap (shows why we need reassociation)
  println("--- Example 4: Nested FlatMap ---")
  val nested =
    TrampolineStruct.Done(1)
      .flatMap(x => TrampolineStruct.Done(x + 1))
      .flatMap(x => TrampolineStruct.Done(x + 1))
      .flatMap(x => TrampolineStruct.Done(x + 1))

  println("Structure:")
  println("FlatMap(")
  println("  FlatMap(")
  println("    FlatMap(")
  println("      Done(1),")
  println("      x => Done(x+1)")
  println("    ),")
  println("    x => Done(x+1)")
  println("  ),")
  println("  x => Done(x+1)")
  println(")")

  println("\nWithout reassociation, interpreter would:")
  println("1. Match outer FlatMap")
  println("2. Recursively interpret inner FlatMap (NOT tail recursive!)")
  println("3. Recursively interpret innermost FlatMap")
  println("4. Get result, apply continuation")
  println("5. This creates stack frames!")

  println("\nWith reassociation:")
  println("1. Match FlatMap(FlatMap(FlatMap(Done(1), f1), f2), f3)")
  println("2. Reassociate to: FlatMap(Done(1), x => f1(x).flatMap(f2).flatMap(f3))")
  println("3. Now we have FlatMap(Done(1), composed)")
  println("4. This is tail recursive!")
  println(s"\nResult: ${nested.run}\n")

  // Example 5: Tracing execution
  println("--- Example 5: Detailed Execution Trace ---")

  var traceStep = 0
  def traced[A](name: String, t: TrampolineStruct[A]): TrampolineStruct[A] =
    TrampolineStruct.More(() => {
      traceStep += 1
      println(s"  [Step $traceStep: $name]")
      t
    })

  val traced_computation =
    traced("Start", TrampolineStruct.Done(1))
      .flatMap(x => traced(s"After 1, got $x", TrampolineStruct.Done(x + 1)))
      .flatMap(x => traced(s"After 2, got $x", TrampolineStruct.Done(x + 1)))

  println("Running traced computation:")
  val result = traced_computation.run
  println(s"Final result: $result\n")

  println("--- The Key Insight ---")
  println("The run method is @tailrec - it never grows the stack!")
  println("It processes the data structure ITERATIVELY")
  println("Each recursive call in run is in TAIL POSITION")
  println("The Scala compiler converts this to a loop")
  println("\nStack depth = O(1), regardless of chain length!")