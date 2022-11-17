package FPIntro

import scala.annotation.tailrec

/*
 *
 *  Copyright (c) 2021. Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *   
 *   Unless required by applicable law or agreed to in writing, software distributed under
 *   the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *   either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *  
 */

object MyFoldLeft:
  @tailrec
  def myFoldLeft(lst: List[Int])(acc: Int)(f: (Int, Int) => Int): Int =
    lst match
      case List() => acc
      case hd :: tl => myFoldLeft(tl)(f(hd, acc))(f)

  @main def runFoldImpl(): Unit =
    println(myFoldLeft(List(1, 3, 5))(0)((acc, elem) => acc + elem))
