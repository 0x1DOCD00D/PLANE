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

/*
* https://en.wikipedia.org/wiki/Semigroup
* In mathematics, a semigroup is an algebraic structure consisting of a set together with an associative internal binary operation on it.
* The binary operation of a semigroup is most often denoted multiplicatively: x·y, or simply xy, denotes the result of applying the semigroup
* operation to the ordered pair (x, y). Associativity is formally expressed as that (x·y)·z = x·(y·z) for all x, y and z in the semigroup.
* Semigroups may be considered a special case of magmas, where the operation is associative, or as a generalization of groups, without requiring
* the existence of an identity element or inverses.[note 1] As in the case of groups or magmas, the semigroup operation need not be commutative,
* so x·y is not necessarily equal to y·x; a well-known example of an operation that is associative but non-commutative is matrix multiplication.
* If the semigroup operation is commutative, then the semigroup is called a commutative semigroup or (less often than in the analogous case of groups)
* it may be called an abelian semigroup.
* A monoid is an algebraic structure intermediate between semigroups and groups, and is a semigroup having an identity element,
* thus obeying all but one of the axioms of a group: existence of inverses is not required of a monoid. A natural example is strings with
* concatenation as the binary operation, and the empty string as the identity element. Restricting to non-empty strings gives an example of a semigroup
* that is not a monoid. Positive integers with addition form a commutative semigroup that is not a monoid, whereas the non-negative integers do form a monoid.
* A semigroup without an identity element can be easily turned into a monoid by just adding an identity element. Consequently, monoids are studied in the theory
* of semigroups rather than in group theory. Semigroups should not be confused with quasigroups, which are a generalization of groups in a different direction;
* the operation in a quasigroup need not be associative but quasigroups preserve from groups a notion of division. Division in semigroups (or in monoids) is not
* possible in general.
  trait Semigroup[A] {
    def combine(x: A, y: A): A
  }
  trait Monoid[A] extends Semigroup[A] {
    def empty: A
  }
* */
object MonoidsSemigroups:
  import cats.Semigroup
  import cats.Monoid
  import cats.instances.*
  import cats.syntax.semigroup.*
  import cats.syntax.monoid.*
  import cats.syntax.option.*
  import cats.syntax.eq.*

  case class TeachingClass(classID: Int, professor: String)
  given Semigroup[Option[TeachingClass]] with {
    override def combine(x: Option[TeachingClass], y: Option[TeachingClass]): Option[TeachingClass] =
      (x, y) match
        case (Some(t1), Some(t2)) => if t1.classID === t2.classID then Some(TeachingClass(t1.classID, t1.professor |+| ", " |+| t2.professor)) else None
        case (Some(t1), None) => x
        case (None, Some(t2)) => y
        case (None, None) => None
  }

  @main def runMainMonoidsSemigroups(): Unit =
    val res1 = Monoid[String] combine("Howdy,", " Mark")
    val res2 = Semigroup[String] combine("Doing well,", " Mark")
    val res3 = "Using special syntax" |+| " to join strings"
    println(res1)
    println(res2)
    println(res3)
    println(Option(10) |+| 10.some)
    println(Map(1->"Mark", 2->"Tina") |+| Map(3->"Golda", 100->"Fima"))
    println(("elem1", 1) |+| ("elem2", 2))
    println(TeachingClass(474, "Mark").some |+| TeachingClass(474, "Dick").some)
    println(TeachingClass(441, "Mark").some |+| TeachingClass(474, "Dick").some)
    println(TeachingClass(441, "Mark").some |+| None)
