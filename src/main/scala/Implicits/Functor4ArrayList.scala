package Implicits

import java.util

/*
 *
 *  Copyright (c) 2021. Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *   
 *   Unless required by applicable law or agreed to in writing, software distributed under
 *   the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *   either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *  
 */

object Functor4ArrayList extends App {

  trait Functor[F[_]]{
    def map[S,T](container: F[S])(f: S=>T):F[T]
  }

  val testList = new util.ArrayList(java.util.Arrays.asList("this", "is", "a", "test"))
//  val transformed = testList.map(_.toUpperCase)


}
