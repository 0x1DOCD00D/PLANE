package CatsIO

import cats.data.State
import cats.effect.{ExitCode, IO, IOApp}
import cats.Monad
import cats.instances.option.*
import cats.instances.list.*

object StateComposition extends IOApp:
  trait CodeProcessorState
  case class Parser(code: List[String]) extends CodeProcessorState
  case class ParsedCode(code: List[String]) extends CodeProcessorState
  case class Translator(code: String) extends CodeProcessorState
  case class Optimizer(tr: List[Translator], code: List[String]) extends CodeProcessorState

  case class ValueCollection(v:CodeProcessorState)

  /*
  * Each function is a transition between the states.
  * The goal of this exercise is to construct a parsing state machine that results in translating some document into a list of records, i.e., case classes.
  * First, we obtain a parser and then we simulate applying this parser to a document. The output is a list of records.
  * Next, depending on the extracted values we switch to different states where lambda functions are applied to traverse and extract specific objects.
  * */

  def log[T] (message : String, instance : T): T =
    println(message + ": " + instance.toString)
    instance
  end log

  def parseTheCode(newcode: String): State[CodeProcessorState, ValueCollection] = State { codeState =>
    println(s"inside state: $codeState")
    println(s"inside newcode: $newcode")
    codeState match
      case p:Parser =>
        val state = ParsedCode(p.code ::: newcode.split(" ").toList)
        (state, ValueCollection(state))
      case p:ParsedCode =>
        val state = Translator((p.code ::: newcode.split(" ").toList).mkString(", "))
        (state, ValueCollection(state))
      case t:Translator =>
        val state = Optimizer(List(t), List(t.code) ::: newcode.split(" ").toList)
        (state, ValueCollection(state))
  }

  val randomInt: State[Long, Int] = State { seed =>
    val rnd = new scala.util.Random(seed)
    (math.abs(rnd.nextLong()), math.abs(rnd.nextInt()))
  }

  override def run(args: List[String]): IO[ExitCode] =
    val randGen = for {
      r1 <- randomInt
      l1 = log("r1: ", r1.toString)
      r2 <- randomInt
      l2 = log("r2: ", r2.toString)
      r3 <- randomInt
      l3 = log("r3: ", r3.toString)
    } yield (r1, r2, r3)

//    4203729218974371943, (1761283695,1243602254,1336550754)
    val (st, r) = randGen.run(1).value
    println((st.toString == "4203729218974371943").toString + ", " + (r == (1761283695,1243602254,1336550754)).toString)

    val processor = for {
      one <- parseTheCode("one two three")
      l1 = log("one: ", one.toString)
      two <- parseTheCode("four five")
      l2 = log("two: ", two.toString)
      three <- parseTheCode("six")
      l2 = log("three: ", three.toString)
    } yield (one, two, three)

    IO.println(processor.run(Parser(List("start and go!"))).value).as(ExitCode.Success)
