package Cats

/*
 *
 *  Copyright (c) 2021. Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *   
 *   Unless required by applicable law or agreed to in writing, software distributed under
 *   the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *   either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *  
 */

object WriterMonad:
  import cats.data.Writer
  import cats.syntax.all.*
  import cats.instances.int.*
  import cats.instances.list.*
  import cats.instances.vector.*
  import cats.syntax.applicative.*
  import cats.implicits.*
  import cats.Monad
//  Attach log entries to operations, functions/methods return logs with computed values.

  def op1(i:Int): Writer[List[String], Int] =
    for {
      v1 <- Writer(List(s"Input is specified $i"), i)
      _ <- List("this is an ", "identity", "operation").tell
      v2 <- (v1*2).writer(List("Double the value"))
    } yield v2

  def op2(i: String): Writer[List[String], Double] =
    for {
      v1 <- Writer(List(s"op2 input is specified $i"), i.toFloat)
      v2 <- math.log(v1).writer(List("Log it"))
    } yield v2

  // writer1: cats.
  @main def runMain_WriterMonad$(): Unit =
    println(op1(10))
    val res:Writer[List[String], Double] = for {
      i <- Writer[List[String], Int](List("Input is given!"), 100)
      x <- op1(i)
      y <- op2(x.toString)
    } yield y
    println(res)

