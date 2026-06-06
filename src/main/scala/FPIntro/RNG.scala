package FPIntro

import scala.util.Random

object RNGProgram:
  trait RNG {
    def getNextNumber: (Int, RNG)
  }

//  (University, Student, Class) => (University, Boolean)

  type RNGFunc = RNG => (Int, RNG)

  val f: RNGFunc = (g: RNG) => g.getNextNumber

  def mapCombinator(g: RNG)(xForm: Int => String): (String, RNG) = {
    val nextGen = g.getNextNumber
    (xForm(nextGen._1), nextGen._2)
  }

  case class ConcreteRNG(seed: Int) extends RNG:
    val rngObject = new Random(seed)
    override def getNextNumber: (Int, RNG) = (rngObject.nextInt(), this)


  def main(args: Array[String]): Unit = {
    val getRand = ConcreteRNG(10)
    val firstRN = f(getRand)//getRand.getNextNumber
    val secondRN = f(firstRN._2)
    val thirdRN = f(secondRN._2)
    val forthRN = f(thirdRN._2)
    println(firstRN._1)
    println(secondRN._1)
    println(thirdRN._1)
    println(forthRN._1)
    println(mapCombinator(forthRN._2)((i:Int)=>(i.toBinaryString + "," + i.toString))._1)
//    -1157793070
    //1913984760
    //1107254586
    //1773446580
  }