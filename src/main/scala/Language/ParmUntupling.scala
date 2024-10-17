package Language

object ParmUntupling:
  val lst: List[(Int, Int)] = 
    for {
      a <- (1 to 5).toList
      b <- (6 to 10).toList
    } yield (a,b)
  val res: List[Int] = lst.map { (x, y) => x + y }
  def main(args: Array[String]): Unit = {
    println(lst)
    println(res)
  }