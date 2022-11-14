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
  /*
  * Say we want to add a method called applyAction to some type and this method will perform some action on this type.
  * To so that we can define an extension[T,S](v:T) with def applyAction:S so that we can call 3.applyAction.
  * However, to define the method appluAction we need to implement some behavior that acts on the input variable, v.
  * This behavior must be parameterized by the type variable, T and it should be implemented for each used value of T.
  * For this behavior we create the trait Wrapper4ActionOnType and its abstract method def actOnT(v:T): S.
  * Next, we create objects that extend this trait by substituting concrete types and make these objects implicit.
  * Finally, we add the second parameter to the extension to pass implicitly an instance of the implemented object.
  * */
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
    def applyAction:S = e.actOnT(v)


  @main def runTypeClassAction(): Unit =
    println(actOnInt.actOnT(5))
    println(5.applyAction)
    println(SomeWrapper(1).applyAction)
