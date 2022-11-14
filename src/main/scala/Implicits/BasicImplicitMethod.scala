package Implicits

/*
 *
 *  Copyright (c) 2021. Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *   
 *   Unless required by applicable law or agreed to in writing, software distributed under
 *   the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *   either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *  
 */

object BasicImplicitMethod:
  case class Wrapper(v: Float):
    def fancyStringConversion(beforePoint: Int, afterPoint: Int): String =
      s"%${beforePoint}.${afterPoint}f".format(v)

  implicit def convert_implicitly(v: Float): Wrapper = Wrapper(v)

  @main def runBIM =
    println(Wrapper(123.2345).fancyStringConversion(1,2))
    println(123.56789f.fancyStringConversion(1,2))