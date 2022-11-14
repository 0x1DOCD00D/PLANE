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

object TypeClassActionOnType:
  trait Wrapper4ActionOnType[T, S]:
    def actOnT(v:T): S

  case class SomeWrapper(v:Int)

  object actOnInt extends Wrapper4ActionOnType[Int, String]:
    override def actOnT(v: Int): String = s"[${v.toString}]"

  object actOnSomeWrapper extends Wrapper4ActionOnType[SomeWrapper, String] :
    override def actOnT(v: SomeWrapper): String = s"[${v.toString}]"

  implicit object actOnIntImpl extends Wrapper4ActionOnType[Int, String] :
    override def actOnT(v: Int): String = s"[${v.toString}]"

  implicit object actOnSomeWrapperImpl extends Wrapper4ActionOnType[SomeWrapper, String] :
    override def actOnT(v: SomeWrapper): String = s"[${v.toString}]"

  extension[T,S] (v:T)(using e: Wrapper4ActionOnType[T, S])
    def applyAction = e.actOnT(v)


  @main def runTypeClassAction(): Unit =
    println(actOnInt.actOnT(5))
    println(5.applyAction)
    println(SomeWrapper(1).applyAction)
