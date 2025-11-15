//https://users.scala-lang.org/t/extending-an-inner-class/12114


////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scala3Features.NewTypes
// Complete demonstration of extending inner classes in Scala 3
// Using traits, objects, and self-types

object ExtendInnerClass:
  // Main trait with type parameter T
  trait A[T]:
    def f: B

    // Inner trait B has access to T from outer scope
    trait B:
      // Method using the outer type parameter T
      def g(x: T): Seq[T] = Seq.fill(m)(x)

      def n: Int
      def m: Int

  // Object implementing A[String]
  object O extends A[String]:
    def f: B =
      // Mix AB with B - AB's self-type requires it to be mixed with A[?]#B
      new AB with B:
        def n: Int = 1
  // m is inherited from AB: m = n + 1 = 2
  // g is inherited from B: creates Seq of 2 elements

  // Trait using self-type to work with inner trait B
  trait AB:
    this: A[?]#B =>  // Self-type: this must also be an A[?]#B
    // Can reference 'n' from B without declaring it
    def m: Int = n + 1

// ============================================
// Extended Examples: Different Use Cases
// ============================================

object ExtendedExamples:

  // Example 1: Multiple implementations with different types
  trait Container[T]:
    def create: Storage

    trait Storage:
      def store(items: Seq[T]): Unit =
        println(s"Storing ${items.size} items of type ${items.headOption.map(_.getClass.getSimpleName).getOrElse("unknown")}")
        performStore(items)

      def performStore(items: Seq[T]): Unit
      def capacity: Int

  // Reusable storage implementation using self-type
  trait LimitedStorage:
    this: Container[?]#Storage =>

    def capacity: Int = maxItems * 2
    def maxItems: Int

  object IntContainer extends Container[Int]:
    def create: Storage =
      new LimitedStorage with Storage:
        def maxItems: Int = 50
        def performStore(items: Seq[Int]): Unit =
          println(s"Stored integers: ${items.take(3).mkString(", ")}...")

  object StringContainer extends Container[String]:
    def create: Storage =
      new LimitedStorage with Storage:
        def maxItems: Int = 100
        def performStore(items: Seq[String]): Unit =
          println(s"Stored strings: ${items.take(3).mkString(", ")}...")

  // Example 2: Multiple abstract classes with different behaviors
  trait DataSource[T]:
    def getProcessor: Processor

    trait Processor:
      def process(data: T): String
      def retries: Int
      def timeout: Int

  // Different processor implementations using traits
  trait QuickProcessor:
    this: DataSource[?]#Processor =>
    def timeout: Int = retries * 100  // Quick timeout

  trait SlowProcessor:
    this: DataSource[?]#Processor =>
    def timeout: Int = retries * 1000  // Longer timeout

  object NetworkDataSource extends DataSource[String]:
    def getProcessor: Processor =
      new SlowProcessor with Processor:
        def retries: Int = 3
        def process(data: String): String =
          s"Processed: $data (timeout: ${timeout}ms)"

  object CacheDataSource extends DataSource[Int]:
    def getProcessor: Processor =
      new QuickProcessor with Processor:
        def retries: Int = 1
        def process(data: Int): String =
          s"Cached: $data (timeout: ${timeout}ms)"

  // Example 3: Complex composition with multiple traits
  trait Repository[T]:
    def getHandler: Handler

    trait Handler:
      def save(item: T): Unit
      def validate(item: T): Boolean
      def maxRetries: Int

  trait RetryLogic:
    this: Repository[?]#Handler =>

    def attemptSave(item: Any): Unit =
      var attempts = 0
      while attempts < maxRetries do
        println(s"Attempt ${attempts + 1} of $maxRetries")
        attempts += 1

  trait ValidationLogic:
    this: Repository[?]#Handler =>

    // Helper method that works with the constraint
    // Since we don't know the exact T, we work at the Any level
    def preValidate(item: Any): Boolean =
      println(s"Pre-validation check on: $item")
      true

  object UserRepository extends Repository[String]:
    def getHandler: Handler =
      new RetryLogic with ValidationLogic with Handler:
        def maxRetries: Int = 3
        def save(item: String): Unit =
          if preValidate(item) then
            println(s"Saving user: $item")
            attemptSave(item)

        def validate(item: String): Boolean =
          item.nonEmpty && item.length > 2

  // Example 4: Practical builder pattern
  trait Builder[T]:
    def build: Component

    trait Component:
      def configure(): Unit =
        println(s"Configuring with ${getParameters.size} parameters")

      def getParameters: Map[String, Any]
      def name: String

  trait BaseComponent:
    this: Builder[?]#Component =>

    def getParameters: Map[String, Any] =
      Map(
        "name" -> name,
        "timestamp" -> System.currentTimeMillis(),
        "version" -> "1.0"
      )

  object HttpBuilder extends Builder[String]:
    def build: Component =
      new BaseComponent with Component:
        def name: String = "HttpComponent"

  object DatabaseBuilder extends Builder[Int]:
    def build: Component =
      new BaseComponent with Component:
        def name: String = "DatabaseComponent"

        // Override to add more parameters
        override def getParameters: Map[String, Any] =
          super.getParameters ++ Map("connectionPool" -> 10)


  @main def runExtendInnerClass(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/scala3Features/NewTypes/ExtendInnerClass.scala created at time 8:56PM")
    println("=" * 60)
    println("ORIGINAL EXAMPLE: ExtendInnerClass")
    println("=" * 60)

    val b1 = ExtendInnerClass.O.f
    println(s"n = ${b1.n}")
    println(s"m = ${b1.m}")
    println(s"g(\"hello\") = ${b1.g("hello")}")

    println("\n" + "=" * 60)
    println("EXAMPLE 1: Container with Storage")
    println("=" * 60)

    val intStorage = ExtendedExamples.IntContainer.create
    println(s"Int storage capacity: ${intStorage.capacity}")
    intStorage.store(Seq(1, 2, 3, 4, 5))

    val stringStorage = ExtendedExamples.StringContainer.create
    println(s"String storage capacity: ${stringStorage.capacity}")
    stringStorage.store(Seq("apple", "banana", "cherry"))

    println("\n" + "=" * 60)
    println("EXAMPLE 2: DataSource with Processors")
    println("=" * 60)

    val networkProcessor = ExtendedExamples.NetworkDataSource.getProcessor
    println(networkProcessor.process("API Response"))

    val cacheProcessor = ExtendedExamples.CacheDataSource.getProcessor
    println(cacheProcessor.process(42))

    println("\n" + "=" * 60)
    println("EXAMPLE 3: Repository with Handler")
    println("=" * 60)

    val userHandler = ExtendedExamples.UserRepository.getHandler
    println(s"Validating 'John': ${userHandler.validate("John")}")
    userHandler.save("John Doe")

    println("\n" + "=" * 60)
    println("EXAMPLE 4: Builder Pattern")
    println("=" * 60)

    val httpComponent = ExtendedExamples.HttpBuilder.build
    println(s"HTTP Component: ${httpComponent.name}")
    httpComponent.configure()
    println(s"Parameters: ${httpComponent.getParameters}")

    val dbComponent = ExtendedExamples.DatabaseBuilder.build
    println(s"\nDB Component: ${dbComponent.name}")
    dbComponent.configure()
    println(s"Parameters: ${dbComponent.getParameters}")

    println("\n" + "=" * 60)
    println("KEY INSIGHTS:")
    println("=" * 60)
    println("1. Self-types (this: A[?]#B =>) allow abstract classes to")
    println("   reference members from inner traits without direct inheritance")
    println("2. Path-dependent types enable inner traits to access outer type parameters")
    println("3. Mixing 'AB with B' satisfies the self-type constraint")
    println("4. This pattern enables powerful composition and code reuse")
