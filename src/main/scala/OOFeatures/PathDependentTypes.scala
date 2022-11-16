package OOFeatures

import scala.collection.mutable
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

object PathDependentTypes:
  trait StorageOfValuesOfTheType:
    type SomeTypeAlias
    val storeData: mutable.ListBuffer[SomeTypeAlias] = ListBuffer()

  trait ParameterizedStorage[StoredDataType] extends StorageOfValuesOfTheType:
    override type SomeTypeAlias = StoredDataType


  trait StorageOfInts extends ParameterizedStorage[Int]
  trait StorageOfString extends ParameterizedStorage[String]

  def retrieveLastStoredObject(someStorageObject: StorageOfValuesOfTheType): StorageOfValuesOfTheType#SomeTypeAlias = someStorageObject.storeData.last

  @main def runPathDependentTypes: Unit =
    val storage = new StorageOfString {}
    storage.storeData += "Mark"
    storage.storeData += "Tina"
    println(retrieveLastStoredObject(storage))
