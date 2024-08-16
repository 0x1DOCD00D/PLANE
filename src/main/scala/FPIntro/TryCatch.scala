package FPIntro

import scala.util.Try

/*
 *
 *  Copyright (c) 2021. Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *   
 *   Unless required by applicable law or agreed to in writing, software distributed under
 *   the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *   either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *  
 */

object TryCatch:
  lazy val exVar:Int = throw new Exception("bam!")//without lazy an exception is thrown eagerly

  def aSimpleFunc(a:Int):Int =
    try
      val b = 1
      b + a
    catch
      case e: Exception =>
        println(s"caught exception $e")
        0
      case _ => 1
    finally
      a + exVar

  @main def runMain_TryCatch$(): Unit =
    println(Try(exVar))
    println(aSimpleFunc(10))
