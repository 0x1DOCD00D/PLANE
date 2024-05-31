package DSLWorkshop

import scala.Dynamic
import scala.language.dynamics
import scala.language.postfixOps

object TypeMatchExample:
  val field = new GenericConstruct
  class GenericConstruct extends Dynamic {
    infix def selectDynamic(name: String): "tag" | "attrs" | "children" = {
      println(s"construct: $name")
      if name == "tag" then "tag"
      else if name == "attrs" then "attrs"
      else "children"
    }
  }

  type HtmlNodeRecord[X] = X match {
    case "tag" => String
    case "attrs" => List[(String, String)]
    case "children" => List[HtmlNode]
  }

  case class HtmlNode(tag: String, attrs: List[(String, String)], children: List[HtmlNode]) {
    def apply(s: "tag" | "attrs" | "children"): HtmlNodeRecord[s.type] =
      s match {
        case _: "tag" => tag
        case _: "attrs" => attrs
        case _: "children" => children
    }
  }

  def main(args: Array[String]): Unit = {
    val html = HtmlNode("tag", List(("whatever", "x")), List())(field attrs)
    println(html)
  }
