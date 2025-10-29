package FPIntro

import FPIntro.Match4UnionTypes.U.{I, S}

import scala.reflect.TypeTest

//https://users.scala-lang.org/t/exhaustive-match-for-union-type/12092
object Match4UnionTypes:
  enum U:
    case S(s: String)
    case I(i: Int)

  type UnionSI = S | I

  object UnionSI {

    opaque type uS = String
    opaque type uI = Int

    object uS:
      inline def apply(inline s: uS): uS = s

      inline def unapply(inline s: uS): Some[String] = Some(s)

      given TypeTest[Any, uS] with
        def unapply(x: Any): Option[uS & x.type] =
          println(s"unapply: ${x.getClass}")
          x match
            case s: String => Some(s.asInstanceOf[uS & x.type])
            case _         => None

    object uI:
      inline def apply(inline i: uI): uI = i

      inline def unapply(inline i: uI): Some[Int] = Some(i)

      given TypeTest[Any, uI] with
        def unapply(x: Any): Option[uI & x.type] =
          println(s"unapply: ${x.getClass}")
          x match
            case i: uI => Some(i.asInstanceOf[uI & x.type])
            case s: uS    => None
  }

  @main def runMatch4UnionTypes(args: String*): Unit =
    println(
       "File /Users/drmark/IdeaProjects/PLANE/src/main/scala/FPIntro/Match4UnionTypes.scala created at time 1:12PM"
    )
    val u: UnionSI = S("hello")
    val v: UnionSI = I(123)
    u match
      case S(s) => println(s)
      case I(i) => println(i)

    v match
      case S(s) => println(s)
      case I(i) => println(i)
