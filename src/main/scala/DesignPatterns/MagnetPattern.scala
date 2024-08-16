package DesignPatterns

/*
 *
 *  Copyright (c) 2021. Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *   
 *   Unless required by applicable law or agreed to in writing, software distributed under
 *   the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *   either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *  
 */

object MagnetPattern:
  def f(p:List[Int]): Int = 1
/*
Double definition:
def f(p: List[Int]): Nothing in object MagnetPattern at line 14 and
def f(p: List[String]): Nothing in object MagnetPattern at line 15
have the same type after erasure.

Consider adding a @targetName annotation to one of the conflicting definitions
for disambiguation.
  def f(p:List[String]) = ???
*/

// This wrapper data types allows us to hold different results
// and avoid the problem with method overloading
  trait MagnetDataType[ResultHolder]:
    def apply(p: List[ResultHolder]): ResultHolder

  given MagnetDataType[Int] with {
    override def apply(p: List[Int]): Int = p.sum
  }

  given MagnetDataType[String] with {
    override def apply(p: List[String]): String = p.foldLeft("")((acc, elem)=> acc.concat(elem) )
  }

//  instead of overloading the method h for different parameterizations of List
// we create its single signature with the magnet data type.
  def h[T](p: List[T])(using e:MagnetDataType[T]):T = e(p)

  @main def runMagnetPattern():Unit =
    println(h(List("a","b","c")))
    println(h(List(3,7)))

