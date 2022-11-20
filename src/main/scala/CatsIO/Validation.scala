package CatsIO

import cats.Semigroup
import cats.data.Validated.{Invalid, Valid}
import cats.data.{Validated, ValidatedNel}
import cats.instances.string.*
import cats.syntax.all.catsSyntaxSemigroup
import cats.syntax.validated.*

object Validation:
  @main def runvalid(): Unit =
    val legitValue: ValidatedNel[List[String], Int] = 1.validNel[List[String]]
    val anErrorMessage: ValidatedNel[String, Int] = "It is an error message".invalidNel
    val aMixedList = List(1.validNel, 2.validNel, "e1".invalidNel, 3.validNel, "e2".invalidNel, 7.validNel)
    println(legitValue)
    println(anErrorMessage)
    println(aMixedList)
    println(aMixedList.filter(_.isValid))
    println(aMixedList.filterNot(_.isValid))
    val res = aMixedList.filterNot(_.isValid).foldLeft(anErrorMessage) {
      (acc, elem) =>
        acc.combine(elem)
    }
    println(res)

    val resvalid = aMixedList.filter(_.isValid).foldLeft(List[Int]()) {
      (acc, elem) =>
        elem.toEither.toOption.get :: acc
    }
    println(resvalid)

