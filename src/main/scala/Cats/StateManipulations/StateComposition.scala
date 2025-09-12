////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.StateManipulations

import cats.Monad
import cats.data.State
import cats.effect.{ExitCode, IO, IOApp}
import cats.instances.list.*
import cats.instances.option.*

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

  import cats.syntax.all.catsSyntaxMonad


  def extractNextItem(result: (List[String], Boolean)): State[List[Int], (List[String], Boolean)] = State { state =>
    println(s"state: $state")
    if (state.isEmpty) (Nil, (result._1,false))
    else
      (state.tail, (s"s_${state.head.toString}" :: result._1, true))
  }
  def process(i: (List[String],Boolean)): Option[(List[String], Boolean)] =
    println(s"processing $i")
    if i._2 then Some(i) else None

  def loop(in: State[List[Int], (List[String],Boolean)]): State[List[Int], (List[String],Boolean)] =
//    val choice: State[List[Int], Int] = extractNextItem map process
    in map process flatMap { i =>
      println(s"i: $i")
      if i.isEmpty then in
      else loop(extractNextItem(i.get))
    }

  override def run(args: List[String]): IO[ExitCode] =
    val rloop = loop(extractNextItem((List.empty,true))).run(List.tabulate(10)(i=>i)).value
    println(s"rloop: $rloop")

    val stateListOfInts = State[List[Int], Int] { state =>
      (state.tail, state.head)
    }
    println(stateListOfInts.run(List(1, 2, 3)).value)

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
