package Scala3FromItsDoc

object PartialFunctions:
  def accumulate[A, B](pfs: PartialFunction[A, B]*): PartialFunction[A, B] = {
    pfs.reduce(_ orElse _) 
  }

  def filter[A](list: List[A])(pf: PartialFunction[A, _]): List[A] = {
    list.collect {
      case x if pf.isDefinedAt(x) => x
    }
  }

  def main(args: Array[String]): Unit = {
    val list = List(1, 2, 3, 4, 5, 6)

    // Define partial functions
    val pf1: PartialFunction[Int, String] = {
      case x if x % 2 == 0 => s"$x is even"
    }

    val pf2: PartialFunction[Int, String] = {
      case x if x % 3 == 0 => s"$x is divisible by 3"
    }

    val pf3: PartialFunction[Int, String] = {
      case x if x > 4 => s"$x is greater than 4"
    }

    // Accumulate the partial functions
    val accumulatedPF = accumulate(pf1, pf2, pf3)

    // Apply the accumulated partial function to filter the list
    val result = filter(list)(accumulatedPF)

    println(result) // Output: List(2, 3, 4, 5, 6)

  }