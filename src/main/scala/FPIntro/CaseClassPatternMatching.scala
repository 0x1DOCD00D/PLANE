package FPIntro

object CaseClassPatternMatching {
  trait CCAll
  case class CC1(v1c1: List[CCAll]) extends CCAll
  case class CC2(v1c2: Int, v2c2: List[CCAll]) extends CCAll
  case class CC3(v1c3: List[CCAll], v2c3: List[CCAll]) extends CCAll

/*  def process(i: List[List[CCAll]]) =
    i map()*/

  @main def runCCPatternMatching(): Unit =
    val rec1 = CC3(List(CC3(List(CC2(1, List(CC1(List(CC3(List(), List(CC2(2, List())))))))),
      List())), List(CC1(List(CC1(List(CC2(3, List())))))))
    val rec2 = CC1(List(CC2(4, List(CC3(List(CC2(5, List(CC1(List())))),
      List(CC3(List(), List(CC1(List())))))))))
    val rec: List[List[CCAll]] = List(List(rec1), List(rec2, rec1))


}
