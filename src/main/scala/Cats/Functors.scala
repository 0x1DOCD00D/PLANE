package Cats

import com.github.nscala_time.time.Imports.DateTime

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
* A category is formed by two sorts of objects, the objects of the category, and the morphisms, which relate
* two objects called the source and the target of the morphism. One often says that a morphism is an arrow
* that maps its source to its target. Morphisms can be composed if the target of the first morphism equals the source
* of the second one, and morphism composition has similar properties as function composition
* (associativity and existence of identity morphisms). Morphisms are often some sort of function, but this is not always the case.
* For example, a monoid may be viewed as a category with a single object, whose morphisms are the elements of the monoid.
* The second fundamental concept of category is the concept of a functor, which plays the role of a morphism
*  between two categories {\displaystyle C_{1}}C_{1} and {\displaystyle C_{2}:}{\displaystyle C_{2}:} it maps
*  objects of {\displaystyle C_{1}}C_{1} to objects of {\displaystyle C_{2}}C_{2} and
*  morphisms of {\displaystyle C_{1}}C_{1} to morphisms of {\displaystyle C_{2}}C_{2} in such a way that sources are mapped
*  to sources and targets are mapped to targets (or, in the case of a contravariant functor, sources are mapped to targets and vice-versa).
* A third fundamental concept is a natural transformation that may be viewed as a morphism of functors.
* https://en.wikipedia.org/wiki/Category_theory
* */
object Functors :
  import cats.Functor
  import cats.syntax.all.toFunctorOps

  case class JobRecord[T](company: T, start: DateTime, end: DateTime, position: T)
  given Functor[JobRecord] with {
    override def map[A, B](fa: JobRecord[A])(f: A => B): JobRecord[B] = JobRecord(f(fa.company), fa.start, fa.end, f(fa.position))
  }

  @main def runMainFunctors(): Unit =
    import com.github.nscala_time.time.Implicits.richDateTime
    import com.github.nscala_time.time.Implicits.richInt
    val newJR = JobRecord(1, DateTime.now(), DateTime.now()+(1.hours + 60.minutes + 12.seconds), 2).map(_+1)
    println(newJR)
