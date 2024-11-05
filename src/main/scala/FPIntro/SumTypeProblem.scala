package FPIntro

object SumTypeProblem:
  sealed trait Looping

  case class Next(value: Looping) extends Looping

  case object End extends Looping

  def paradoxicalLoop(looping: Looping): String = looping match {
    case End => "Reached End"
    case Next(nextVal) => paradoxicalLoop(nextVal)
    case v:Looping => paradoxicalLoop(Next(v))
  }

  def main(args: Array[String]): Unit = {
    println {
      paradoxicalLoop(Next(new Looping {}))
    }
   }