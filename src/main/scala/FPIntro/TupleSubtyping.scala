package FPIntro

object TupleSubtyping:
  class A

  class A1 extends A

  class B

  class B1 extends B

  val specificTuple: (A1, B1) = (new A1, new B1) // Introduction: Constructing a specific tuple
  val generalTuple: (A, B) = specificTuple
  val generalTupleA1: (A1, B) = specificTuple
  val generalTupleB1: (A, B1) = specificTuple

  def main(args: Array[String]): Unit = {
    println(generalTupleA1._1)
    println(generalTupleB1._2)
  }