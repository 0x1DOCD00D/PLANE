package Cats

import cats.Applicative
import cats.data.EitherT
import cats.syntax.all.*
import cats.effect.*
import cats.syntax.either.*
import cats.effect.unsafe.implicits.global
import Aid4Debugging.*
import cats.effect.kernel.Outcome.{Canceled, Errored, Succeeded}

import java.io.File
import scala.io.Source

case object ParserNotFoundError
case class ParserErrorMessage(code:Int, msg:String)
case class Parser(i:Int)

class EitherValues[F[_]: Applicative]:
  def getParserSimpleEither(i:Int): Either[ParserErrorMessage, Parser] = if i%2 == 0 then ParserErrorMessage(1, "it is an even error").asLeft else Parser(i).asRight
  def combineParsers(sp1: Parser, sp2: Parser): Either[ParserErrorMessage, List[Parser]] = if sp1.i > sp2.i then ParserErrorMessage(2, "fight the inequality!").asLeft else List(sp1, sp2).asRight
  def getParser(i:Int): EitherT[F, ParserNotFoundError.type, Parser] = if i%2 == 0 then EitherT.left(ParserNotFoundError.pure[F]) else EitherT.right(Parser(i).pure[F])

object EitherValues extends IOApp:
  val obj = new EitherValues[IO]
  val successParser1: Either[ParserErrorMessage, Parser] = obj.getParserSimpleEither(1)
  val successParser2: Either[ParserErrorMessage, Parser] = obj.getParserSimpleEither(3)
  val failedParser: Either[ParserErrorMessage, Parser] = obj.getParserSimpleEither(2)
  println(successParser1.toString + ", " + failedParser.toString)

  val res1: Either[ParserErrorMessage, List[Parser]] = for {
    sp1 <- successParser1
    sp2 <- successParser2
    result <- obj.combineParsers(sp1, sp2)
  } yield result

  val res2: Either[ParserErrorMessage, List[Parser]] = for {
    sp1 <- successParser2
    sp2 <- successParser1
    result <- obj.combineParsers(sp1, sp2)
  } yield result

  val res3: Either[ParserErrorMessage, List[Parser]] = for {
    sp1 <- successParser1
    sp2 <- failedParser
    result <- obj.combineParsers(sp1, sp2)
  } yield result

  //println(s"res1: ${res1.toString}, res2: ${res2.toString}, res3: ${res3.toString}")

  val number: EitherT[Option, String, Int] = EitherT.rightT(5)
  val error: EitherT[Option, String, Int] = EitherT.leftT("Not a number")
  println(s"number: $number, error: $error")

  val effectAsEither: IO[Either[String, Int]] = Left("bad").pure[IO]
  val effectAsEither1: EitherT[IO,String, Int] = EitherT.left("bad".pure[IO])
  println(effectAsEither)
  println(effectAsEither1)

  val listOfEithers: EitherT[List, String, Int] = EitherT(List(Left("something wrong"), Right(43)))
  println(listOfEithers)

  override def run(args: List[String]): IO[ExitCode] =
    val result_1 = "this is an error message".asLeft
    val result_2 = Parser(2).asRight
    val result_3 = 3.asRight.ensure("It is not even!")(v => v % 2 == 0)
    println(result_3)

    val prg = for {
      a <- effectAsEither1
    } yield a

    println(s"prg value: ${prg.value.unsafeRunSync()}")
    prg.value.unsafeRunSync() match {
      case Left(x) => println(s"left value $x")
      case Right(x) => println(s"right value $x")
    }

    val programLeft = for {
      parser <- IO {
        EitherT.left(ParserNotFoundError.pure[IO])
      }
      result <- parser.value
    } yield result

    val programRight = for {
      parser <- IO {
        EitherT.right(new EitherValues[IO].getParser(3).pure[IO])
      }
      result <- parser.value
    } yield result

    val program = for {
      p1 <- programLeft
      p2 <- programRight
    } yield (p1, p2)


    println(programLeft.unsafeRunSync())
    programLeft.unsafeRunSync() match {
      case Left(x) => println(x)
      case _ => println("howdy")
    }

    programRight.unsafeRunSync() match {
      case Right(x) => println(x)
      case _ => println("howdy")
    }

    println(program.unsafeRunSync())
    programLeft.as(ExitCode.Success)
