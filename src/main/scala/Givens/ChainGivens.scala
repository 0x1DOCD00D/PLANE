package Givens

object ChainGivens:
/*
  implicit class C1(val i: Int) {
    def x(j: Int): Int = i * j
  }

  val result = 5 x 3
  println(result)
*/

  class C1(val i: Int) {
    infix def x(j: Int): Int = i * j
  }

  given useSymbolX4Multiplication: Conversion[Int, C1] with
    def apply(x: Int): C1 = C1(x)
/*
  extension (v:Int)
    infix def x(j:Int) = v+j*/

  val c1:C1 = 3
  class C2[T]
  trait C3[T]
  
//https://www-dev.scala-lang.org/scala3/reference/contextual/givens.html
  trait Ord[T]:
    def compare(x: T, y: T): Int
    extension (x: T) def < (y: T) = compare(x, y) < 0
    extension (x: T) def > (y: T) = compare(x, y) > 0

  given intOrd: Ord[Int] with
    def compare(x: Int, y: Int): Int =
      if x < y then -1 else if x > y then +1 else 0

  given listOrd[T](using ord: Ord[T]): Ord[List[T]] with
    def compare(xs: List[T], ys: List[T]): Int = (xs, ys) match
      case (Nil, Nil) => 0
      case (Nil, _) => -1
      case (_, Nil) => +1
      case (x :: xs1, y :: ys1) =>
        val fst = ord.compare(x, y)
        if fst != 0 then fst else compare(xs1, ys1)


  //  given listOrde4(i:Int)(using ord: List[Int]): C1 with C1(i)

  //given convert2C2FromC1[XX](i: XX)(using xx: XX => C1): C2[XX] with C2[XX]

//  val c2:C2 = 7


  @main def RunChainGivens(): Unit =
    println(2 x 3)
    println(c1.i)
//    println(c2.i)