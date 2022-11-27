package Cats

import cats.data.{OptionT, Writer}

/*
 *
 *  Copyright (c) 2021. Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *   
 *   Unless required by applicable law or agreed to in writing, software distributed under
 *   the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *   either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *  
 */

object Transformers:
  import cats.instances.vector.*
  import cats.instances.future.*
  import cats.syntax.applicative.*
  import cats.syntax.all.catsSyntaxWriterId
  import scala.concurrent.Future
  import cats.data.{EitherT, OptionT}

  //from chapter 5 of the cats textbook
  type FutureEither[A] = EitherT[Future, String, A]//=> Future[Either[String, A]]
  type FutureEitherOption[A] = OptionT[FutureEither, A]
  import cats.instances.future.*
  import scala.concurrent.Await
  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.duration._
  import cats.data.Writer

  type Logged[A] = Writer[List[String], A]

  val futureEitherOr: FutureEitherOption[(Int,Int)] =
    for {
      a <- 10.pure[FutureEitherOption]
      b <- 32.pure[FutureEitherOption]
    } yield (a,b)

  def parseNumber(str: String): Logged[Option[Int]] =//Writer[List[String], Option[Int]]
    util.Try(str.toInt).toOption match {
      case Some(num) => Writer(List(s"Read $str"), Some(num))
      case None => Writer(List(s"Failed on $str"), None)
    }

  def addAll(a: String, b: String, c: String): Logged[Option[Int]] =//Writer[List[String], Option[Int]]
  {
    val result = for {
      a <- OptionT(parseNumber(a))
      b <- OptionT(parseNumber(b))
      c <- OptionT(parseNumber(c))
    } yield a + b + c
    result.value
  }

  @main def runMain_Transformers$(): Unit =
    val vectorOfOptionsOfInts: OptionT[Vector, Int] = OptionT(Vector(Option(1), Option(2)))//Vector[Option[Int]]
    val res = vectorOfOptionsOfInts.map(x=>{
        println(x)
        x
      })
      println(res)
      println(vectorOfOptionsOfInts)
      println(futureEitherOr)
      val r = addAll("1", "5", "what the hell is that?")
      println(r)
      val r1 = addAll("1", "5", "10")
      println(r1)
