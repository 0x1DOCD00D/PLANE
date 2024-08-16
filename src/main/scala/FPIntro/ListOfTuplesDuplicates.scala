package FPIntro

import cats.Eq
import cats.implicits.*
import cats.instances.*
import cats.syntax.*

object ListOfTuplesDuplicates:
  //noinspection ZeppelinScalaResUsageFilter
  @main def runListDuplicatesTuples(): Unit =
    val lst = List(
      ("a", 1, "b"),
      ("b", 1, "c"),
      ("b", 1, "a"),
    )

    val lstOptions = List(
      ("a", 1, Option("b")),
      ("b", 1, Option("c")),
      ("a", 1, Option("a")),
      ("a", 1, Option("d")),
      ("b", 1, None),
      ("f", 1, None),
      ("e", 1, None),
      ("e", 2, None),
      ("a", 1, None)
    )
    val res1 = lstOptions.map(triple => triple(0))
    val res2 = lstOptions.map(triple => triple(2))
    println(lst.groupBy(elem => elem(0)))
    println(res1)
    println(res2)
    println(res2.flatten)
    println(res1.distinct.length === res1.length)
    println(res1.groupBy(identity))
    println(res1.groupBy(identity).collect { case (elem, y: List[_]) => if y.length > 1 then elem.some else None }.flatten)
    //    println(res2.filter(parent => parent =!= None).filterNot(parent => res1.contains(parent)))
    println(res2.flatten.filterNot(parent => res1.contains(parent)))
