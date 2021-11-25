package Language

import scala.util.{Failure, Success, Try}

object EnumExperiments:
  enum ConsistencyModels:
    case STRICT, SEQUENTIAL, CAUSAL, EVENTUAL

  val cv = ConsistencyModels.values.toList
  println(cv)
  val b = Try(ConsistencyModels.valueOf("SEQUENTIAL1".toUpperCase)) match {
    case Success(res) => res
    case Failure(e) => e.getMessage
  }

  val c = Try(ConsistencyModels.valueOf("EVENTUAL".toUpperCase)) match {
    case Success(res) => res
    case Failure(e) => e.getMessage
  }

  @main def runIt = println(b)

  println(c)