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

object FunctionLiteralsApplications:
  val f: Int=>Int=>Int = (a:Int)=>(b:Int)=> a*b

  val res: Int = ((a:Int)=> (b:Int)=> a-b)(3)(5)
  val res_a: Int = ((b:Int)=> 3-b)(5)
  val res_a_b: Int = 3-5

  @main def runMainFuncLitApp(): Unit =
    println(res)
    println(res_a)

