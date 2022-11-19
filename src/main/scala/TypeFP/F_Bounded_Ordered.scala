package TypeFP

/*
 *
 *  Copyright (c) 2021. Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *   
 *   Unless required by applicable law or agreed to in writing, software distributed under
 *   the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *   either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *  
 */

object F_Bounded_Ordered:
  /*
  * Ordered[A] extends Comparable[A]

  A trait for data that have a single, natural ordering. See scala.math.Ordering before using this trait for more information about whether to use scala.math.Ordering instead.

  Classes that implement this trait can be sorted with scala.util.Sorting and can be compared with standard comparison operators (e.g. > and <).

  Ordered should be used for data with a single, natural ordering (like integers) while Ordering allows for multiple ordering implementations. An Ordering instance will be implicitly created if necessary.

  scala.math.Ordering is an alternative to this trait that allows multiple orderings to be defined for the same type.

  scala.math.PartiallyOrdered is an alternative to this trait for partially ordered data.

  For example, create a simple class that implements Ordered and then sort it with scala.util.Sorting:

  case class OrderedClass(n:Int) extends Ordered[OrderedClass] {
    def compare(that: OrderedClass) =  this.n - that.n
  }
  * */
  trait Container[A] extends Ordered[A]

  class MyContainer extends Container[MyContainer] {
    override def compare(that: MyContainer): Int = 0
  }

  class WhoKnowsWhatItIs
  /*
  * Using this code template it is possible to write
  * the following nonsensical code that compares AppleContainer
  * with some OrangeContainer
  * */
  class AppleContainer extends Container[AppleContainer] {
    override def compare(that: AppleContainer) = 1
  }

  class OrangeContainer extends Container[AppleContainer] {
    override def compare(that: AppleContainer) = 1
  }

  class PearContainer extends Container[OrangeContainer] {
    override def compare(that: OrangeContainer) = 1
  }

//  and even worse...
  class ApricotContainer extends Container[WhoKnowsWhatItIs] {
    override def compare(that: WhoKnowsWhatItIs) = 1
  }

///////////////////// let's fix the type constraints to prevent parameterization by some random type
//When a class is derived from the parent Container the latter should be
//parameterized with the name of the derived class. Unfortunately, there is
//no mechanism for this enforcement resulting in the derived class extending
//the parent class Container with a type that is different from the derived class.
//However, if we force the type variable to have the upper bound of the Storage type
//then we can flag the declaration of MyStorageOuch as an error

  trait Storage[A <: Storage[A]] extends Ordered[A]
  class MyStorage4Apples extends Storage[MyStorage4Apples] {
    override def compare(that: MyStorage4Apples): Int = 1
  }

/*
  class MyStorageOuch extends Storage[WhoKnowsWhatItIs] {
    override def compare(that: WhoKnowsWhatItIs): Int = 1
  }
*/

  class MyStorage4Oranges extends Storage[MyStorage4Apples] {
    override def compare(that: MyStorage4Apples): Int = 1
  }

  ///////////////////this is a permanent fix, so that we can only compare containers of the same type
  trait Package[A <: Package[A]] extends Ordered[A]:
    self: A =>

  class PackageOfApples extends Package[PackageOfApples] {
    override def compare(that: PackageOfApples): Int = 1
  }

  /*
  * illegal inheritance: self type TypeFP.F_Bounded_Ordered.PackageOfOranges of class PackageOfOranges does not conform to self type TypeFP.F_Bounded_Ordered.PackageOfApples
  of parent trait Package
  * */
/*
  class PackageOfOranges extends Package[PackageOfApples] {
    override def compare(that: PackageOfApples): Int = 1
  }
*/
  class PackageOfOranges extends Package[PackageOfOranges] {
    override def compare(that: PackageOfOranges): Int = 1
  }

  @main def runMainFunc(): Unit =
    new OrangeContainer().compare(new AppleContainer)//we should not be able to do it
//    new PearContainer().compare(new AppleContainer) //this is rejected, but the next line is ok
    new PearContainer().compare(new OrangeContainer)
//    we should compare a container object with another one only if their types are the same
// but this solution enables us only to ensure that we pass the storage parameter
    new MyStorage4Oranges().compare(new MyStorage4Apples)

    new PackageOfApples().compare(new PackageOfApples)
    println(F_Bounded_Ordered.getClass.getName)

