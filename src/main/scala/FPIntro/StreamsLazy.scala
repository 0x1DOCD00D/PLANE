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

object StreamsLazy extends App {
  trait MyStream[+T]
  case object Empty extends MyStream[Nothing]
  case class Cons[+A](h: () => A, t: () => MyStream[A]) extends MyStream[A]

  object MyStream {
    def apply[A](hd: => A, tl: => MyStream[A]): MyStream[A] = {
      lazy val head = hd
      lazy val tail = tl
      Cons(() => head, () => tail)
    }
  }
  def corecInts(start:Int):LazyList[Int] = start #:: corecInts(start+1)

  /*

    def unfold[S, T](z: S)(f: S => Option[(T, S)]): MyStream[T] =
      f(z) match {
        case Some((head,tail)) => MyStream(head, unfold(tail)(f))
        case None => Empty.asInstanceOf
      }


    def unfold[A, S](z: S)(f: S => Option[(A, S)]): MyStream[A] =
      f(z) match {
        case Some((h,s)) => MyStream(h, unfold(s)(f))
        case None => Empty
      }
  */


    //val onesViaUnfold = unfold(1)(_ => Some((1,1)))

}
