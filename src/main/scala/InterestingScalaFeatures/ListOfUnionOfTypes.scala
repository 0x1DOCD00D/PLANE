package InterestingScalaFeatures

object ListOfUnionOfTypes:
  case class Mark(id: Int)
  @main def runthislist(): Unit =
    val lst: List[Int | String] = List(1, 2, "mark")
//    val lst: List[Int | String] = List(1, 2, "mark", Mark(1))
    println(lst.head.getClass.getName)
    println(lst(2).getClass.getName)