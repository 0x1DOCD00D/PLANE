/*
 *
 *  * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package FPIntro

/*
* In mathematical logic, the Peano axioms, also known as the Dedekind–Peano axioms or the Peano postulates,
* are axioms for the natural numbers presented by the 19th century Italian mathematician Giuseppe Peano.
* These axioms have been used nearly unchanged in a number of metamathematical investigations, including
*  research into fundamental questions of whether number theory is consistent and complete.
* https://en.wikipedia.org/wiki/Peano_axioms#
* */
object PeanoArithmetics extends App {

  trait NaturalNumber

  object Zero extends NaturalNumber

  case class Succ[N <: NaturalNumber](number: N) extends NaturalNumber

  def translate2Arabic(n: NaturalNumber): Option[Int] = {
    def translateAcc(acc: Int, n: NaturalNumber): Option[Int] = {
      n match {
        case Zero => Some(acc)
        case Succ(x) => translateAcc(acc + 1, x)
      }
    }

    translateAcc(0, n)
  }

  def translateFromArabic(n: Int): Option[NaturalNumber] = {
    def translateAcc(acc: NaturalNumber, m: Int): Option[NaturalNumber] = {
      m match {
        case 0 => Some(acc)
        case x if x > 0 => translateAcc(succ(acc), x - 1)
        case _ => None
      }
    }

    translateAcc(Zero, n)
  }


  println(translate2Arabic(Succ(Succ(Succ(Succ(Succ(Zero)))))))
  println(translateFromArabic(10))

  def succ(i: NaturalNumber): NaturalNumber = Succ(i)

  def pred(i: NaturalNumber): Option[NaturalNumber] = i match {
    case Zero => None
    case Succ(x) => Some(x)
  }


  def sum(i: NaturalNumber, j: NaturalNumber): Option[NaturalNumber] = {
    if (j == Zero) Some(i)
    else sum(succ(i), pred(j).get)
  }

  println(sum(Succ(Succ(Succ(Succ(Succ(Zero))))), Succ(Succ(Succ(Succ(Succ(Succ(Succ(Succ(Zero))))))))).get)

  def sub(i: NaturalNumber, j: NaturalNumber): Option[NaturalNumber] = {
    if (j == Zero) Some(i)
    else sub(pred(i).get, pred(j).get)
  }


  println(translate2Arabic(sub(translateFromArabic(17).get, translateFromArabic(8).get).get))
  /*

    def mult(i:NaturalNumber, j:NaturalNumber):Option[NaturalNumber] = {
      def multAcc(acc:NaturalNumber, n:NaturalNumber):Option[NaturalNumber] = {
        if(n == Zero)Some(acc)
        else multAcc(sum(acc,i).get, pred(n))
      }
      if(i < 0 || j < 0) None
      else multAcc(0, j)
    }

    println(mult(5,8).get)

    def div(i:NaturalNumber, j:NaturalNumber):Option[Tuple2[NaturalNumber,NaturalNumber]] = {
      def divResult(result:Int, m:Int, n:Int):Option[Tuple2[Int,Int]] = {
        if(m < n)Some((result,m))
        else divResult(succ(result), sub(m,n).get, n)
      }
      if(i < 0 || j <= 0) None
      else divResult(0, i, j)
    }

    println(div(35,7).get)
    println(div(35,6).get)
  */


}
