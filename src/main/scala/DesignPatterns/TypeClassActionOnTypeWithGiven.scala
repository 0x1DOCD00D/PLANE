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

object TypeClassActionOnTypeWithGiven:
  trait Wrapper4ActionOnType[T, S]:
    def actOnT(v: T): S

  case class SomeWrapper(v: Int)

  object actOnInt extends Wrapper4ActionOnType[Int, String] :
    override def actOnT(v: Int): String = s"[${v.toString}]"

  object actOnSomeWrapper extends Wrapper4ActionOnType[SomeWrapper, String] :
    override def actOnT(v: SomeWrapper): String = s"[${v.toString}]"

  given Wrapper4ActionOnType[Int, String] with {
    override def actOnT(v: Int): String = s"[${v.toString}]"
  }

  given Wrapper4ActionOnType[SomeWrapper, String] with {
    override def actOnT(v: SomeWrapper): String = s"[${v.toString}]"
  }

  extension[T, S] (v: T)(using e: Wrapper4ActionOnType[T, S])
    def applyAction: S = e.actOnT(v)


  @main def runTypeClassActionWithGiven(): Unit =
    println(actOnInt.actOnT(5))
    println(5.applyAction)
    println(SomeWrapper(1).applyAction)
