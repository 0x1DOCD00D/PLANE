package FPIntro

object FoldingStuff:
  @main def runFolding(): Unit =
    val lst = List(1, 2, 3, 4, 5)
    val res = lst.foldLeft(true) {
      (acc, elem) =>
        if elem % 8 == 0 then
          false
        else
          acc
    }
    println(res)