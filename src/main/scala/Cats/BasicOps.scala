package Cats

import cats.{Eq, Show}

/*
 *
 *  Copyright (c) 2021. Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *   
 *   Unless required by applicable law or agreed to in writing, software distributed under
 *   the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *   either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *  
 */

//scala with cats book - chapter 1
object BasicOps:
  import cats.instances.*
  import cats.syntax.show.*
  import cats.syntax.eq.*
  import cats.Eq.*
  import cats.syntax.option.*

  case class Professor(name:String, subject: String, citations: Int)

  given Show[Int] with {
    override def show(t: Int): String = if t > 10000 then "it's a mighty number" else "too small"
  }

  given Show[Professor] with {
    override def show(t: Professor): String = s"[${t.name}] knows about ${t.subject} and has ${t.citations} citations"
  }

  given Eq[Professor] with {
    override def eqv(x: Professor, y: Professor): Boolean = if x.citations === y.citations then true else false
  }

  @main def runMainBasicOps(): Unit =
    val eqInt = Eq[Int]
    println(120466.show)
    println(eqInt.eqv(11,12))
    println(11 =!= 12)
    println(Professor("Mark G", "CS", 4016).show)
    println(1.some === none[Int])
    println(Professor("Mark G", "CS", 4016) === Professor("Funky Spunk", "ECE", 4016))
