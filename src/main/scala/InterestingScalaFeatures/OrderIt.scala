package InterestingScalaFeatures

/*
 *
 *  Copyright (c) 2021. Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *   
 *   Unless required by applicable law or agreed to in writing, software distributed under
 *   the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *   either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *  
 */

object OrderIt:
  case class Wrapper(p1: Int, p2: String)
  val orderSorting: Ordering[Wrapper] = Ordering.fromLessThan((w1, w2)=>w1.p1 < w2.p1 )
  implicit val orderSorting1: Ordering[Wrapper] = Ordering.fromLessThan((w1, w2)=>w1.p1 > w2.p1 )
  @main def runOrderIt =
    println(List(Wrapper(1,"b"), Wrapper(2,"a"), Wrapper(3, "a")).sorted(orderSorting))
    println(List(Wrapper(1,"b"), Wrapper(2,"a"), Wrapper(3, "a")).sorted)
