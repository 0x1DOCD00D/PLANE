package TypeFP

import scala.compiletime.ops.int.+

object TypeMatchExamples:
  type DepthOf[T] <: Int = T match
    case List[t] => 1 + DepthOf[t]
    case _ => 0

  type Append[L1 <: Tuple, L2 <: Tuple] <: Tuple = L1 match
    case EmptyTuple => L2
    case head *: tail => head *: Append[tail, L2]

  type L1 = (Int, String)
  type L2 = (Boolean, Double)
  type AppendedList = Append[L1, L2] // (Int, String, Boolean, Double)

  type ConvertIntToString[T] = T match
    case Int => String
    case List[t] => List[ConvertIntToString[t]]
    case Option[t] => Option[ConvertIntToString[t]]
    case _ => T

  import scala.Tuple.Concat
  type Flatten[T <: Tuple] <: Tuple = T match
    case EmptyTuple => EmptyTuple
    case h *: t => h match
      case Tuple => Concat[Flatten[h], Flatten[t]]
      case _ => h *: Flatten[t]

  def main(args: Array[String]): Unit = {
    val depth0: DepthOf[Int] = 0 // No nesting
    val depth1: DepthOf[List[Int]] = 1 // One level of nesting
    val depth2: DepthOf[List[List[Int]]] = 2 // Two levels of nesting
    val depth3: DepthOf[List[List[List[Int]]]] = 3 // Three levels of nesting

    val tpl:Append[L1, L2] = (1, "a", true, 3.7d)

    type Converted1 = ConvertIntToString[Int] // String
    type Converted2 = ConvertIntToString[Option[Int]] // Option[String]
    type Converted3 = ConvertIntToString[List[Int]] // List[String]
    type Converted4 = ConvertIntToString[Double] // Double

    type Flattened1 = Flatten[(Int, (String, (Boolean, Double)))] // (Int, String, Boolean, Double)
    val fltnd: Flattened1 = (1, "a", true, 7.2d)

  }