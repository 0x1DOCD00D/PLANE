package TypeFP

import scala.collection.mutable.ListBuffer

/*
 *
 *  Copyright (c) 2021. Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *   
 *   Unless required by applicable law or agreed to in writing, software distributed under
 *   the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *   either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *  
 */

object SelfType:
  class WhatTypeIsItHuh

  class Sometype:
    self: WhatTypeIsItHuh =>

  //TypeFP.SelfType.Sometype does not conform to its self type TypeFP.SelfType.WhatTypeIsItHuh; cannot be instantiated
//  val x = new Sometype().getClass

  class B
  class A[T] {
    self: T =>
    val l: ListBuffer[self.type] = ListBuffer()
  }
  @main def runMainSelfType(): Unit =
    println(new A[B]().l.getClass)
//    Found:    TypeFP.SelfType.B
    //Required: Nothing
//    println(new A[B]().l += new B)
