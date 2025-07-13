package TypeFP

import scala.deriving.Mirror
import scala.compiletime.{constValue, constValueTuple, erasedValue, summonInline}

trait SomeEncoder[T]:
  def encode(value: T): String

object SomeEncoder:
  given SomeEncoder[Int] with
    def encode(value: Int): String = s"Int: $value"

  given SomeEncoder[String] with
    def encode(value: String): String = s"String: $value"

  given SomeEncoder[Double] with
    def encode(value: Double): String = s"Double: $value"

  given SomeEncoder[Boolean] with
    def encode(value: Boolean): String = s"Boolean: $value"

  private inline def summonAll[Elems <: Tuple]: List[SomeEncoder[Any]] =
    inline erasedValue[Elems] match
      case _: EmptyTuple => Nil
      case _: (h *: t)   =>
        summonInline[SomeEncoder[h]].asInstanceOf[SomeEncoder[Any]] ::
          summonAll[t]

  inline given derived[T](using m: Mirror.ProductOf[T]): SomeEncoder[T] =
    new SomeEncoder[T]:
      private val elemEncoders = summonAll[m.MirroredElemTypes]
      private val elemLabels   = constValueTuple[m.MirroredElemLabels]
        .toList.asInstanceOf[List[String]]
      private val typeLabel    = constValue[m.MirroredLabel]

      def encode(value: T): String =
        val prod   = value.asInstanceOf[Product].productIterator.toList
        val fields = elemLabels
          .zip(elemEncoders.zip(prod))
          .map { case (lbl, (enc, fld)) =>
            s"$lbl=${enc.encode(fld)}"
          }
          .mkString(", ")
        s"$typeLabel($fields)"

extension [T](value: T)
  def xform(using enc: SomeEncoder[T]): String = enc.encode(value)

case class Student(name: String, age: Int)
case class Course(code: String, credits: Double, active: Boolean)

@main def run(): Unit =
  println("Hello, World!".xform)                     // String: Hello, World!
  println(42.xform)                                 // Int: 42
  println(3.14.xform)                               // Double: 3.14
  println(true.xform)                               // Boolean: true

  val alice = Student("Alice", 25)
  val math  = Course("MATH-200", 4.0, true)

  // `Student` and `Course` pick up a derived encoder automatically
  println(alice.xform)   // Student(name=String: Alice, age=Int: 25)
  println(math.xform)    // Course(code=String: MATH-200, credits=Double: 4.0, active=Boolean: true)
