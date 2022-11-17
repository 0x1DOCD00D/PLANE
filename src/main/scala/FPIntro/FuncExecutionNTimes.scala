package FPIntro

/*
 *
 *  Copyright (c) 2021. Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *   
 *   Unless required by applicable law or agreed to in writing, software distributed under
 *   the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *   either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *  
 */

object FuncExecutionNTimes:
  val _f: Int=>Int = (i:Int)=> i+1
  val f: Int=>Int = (i:Int)=> _f(i)
  val ff: Int=>Int = (i:Int)=> f(_f(i))
  val fff: Int=>Int = (i:Int)=> ff(f(i))

  def applyFuncIndefinitely(f:Int=>Int): Int=>Int = (i:Int)=> applyFuncIndefinitely(f)(f(i))
  def applyFuncNtimes(f:Int=>Int, n:Int): Int=>Int =
    if n == 0 then (i:Int)=>i
    else (i:Int)=>applyFuncNtimes(f,n-1)(f(i))

  @main def runFuncExecutionNTimes():Unit =
    println(fff(5))
    println(applyFuncIndefinitely(_f))
    println(applyFuncNtimes(_f, 10)(5))
//    println(applyFuncIndefinitely(_f)(5))