package TypeFP

/*
 *
 *  Copyright (c) 2021. Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *   
 *   Unless required by applicable law or agreed to in writing, software distributed under
 *   the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *   either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *  
 */

object TypeClass4FBP:
  class Fruit
  class Apple extends Fruit
  class Orange extends Fruit

  extension [T <: Fruit](o: T)
    def compareFruits[S](that: S)(implicit evidence: T =:= S): Int = o.toString.length - that.toString.length

  @main def runMainTC4FBP(): Unit =
//    Cannot prove that TypeFP.TypeClass4FBP.Apple =:= TypeFP.TypeClass4FBP.Orange
//    new Apple().compareFruits(new Orange)
//    new Apple().compareFruits(new Fruit)
    new Fruit().compareFruits(new Fruit)
    new Apple().compareFruits(new Apple) //works fine - only object of the identical types can be compared

    println(TypeClass4FBP.getClass.getName)

