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

object F_Bounded_Fruits:
//  This is obviously a bad solution because programmers can violate basic constraints on the reproduced fruits.
  trait Fruit:
    def reproduce: List[Fruit]

  class Apple extends Fruit {
    override def reproduce: List[Orange] = List(new Orange, new Orange, new Orange)
  }

  class Orange extends Fruit {
    override def reproduce: List[Fruit] = List(new Apple, new Orange, new Fruit {
      override def reproduce: List[Orange] = null
    })
  }

  // doing so solves the problem of reproducing unrelated fruits, but Oorange is not Aaple
  trait FFruit[T <: FFruit[T]]:
    def reproduce: List[T]

  class Aapple extends FFruit[Aapple] {
    override def reproduce: List[Aapple] = List(new Aapple, new Aapple, new Aapple)
  }

  class Oorange extends FFruit[Aapple] {
    override def reproduce: List[Aapple] = List(new Aapple, new Aapple)
  }

//  self type comes to rescue to solve the problem
  trait FFFruit[T <: FFFruit[T]]:
    self: T =>
    def reproduce: List[T]

  class Aaapple extends FFFruit[Aaapple] {
    override def reproduce: List[Aaapple] = List(new Aaapple, new Aaapple, new Aaapple)
  }

//  illegal inheritance: self type TypeFP.F_Bounded_Fruits.Ooorange of class Ooorange does not conform to self type TypeFP.F_Bounded_Fruits.Aaapple
/*
  class Ooorange extends FFFruit[Aaapple] {
    override def reproduce: List[Aaapple] = List(new Aaapple, new Aaapple)
  }
*/

  @main def runMainFBFruits(): Unit =
    println(F_Bounded_Fruits.getClass.getName)

