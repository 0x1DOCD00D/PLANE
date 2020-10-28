/*
 *
 *  * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package TypeFP

object SomeDataStorage {

  trait MyDataStorage[D] {
    def foldLeft[T](zero: T)(func: (T, D) => T): T

    def howMany: Int
  }

  trait Measure[S <: Measure[S]] {
    def value: Int

    def unit(measure: Int): S

    def combine(m: S): S = unit(value + m.value)
  }

  class HardDriveSpace(gb: Int) extends Measure[HardDriveSpace] {
    override def value: Int = gb

    override def unit(measure: Int): HardDriveSpace = new HardDriveSpace(measure)
  }

  //  implicit val hd = new HardDriveSpace(12)
  //  def contextBoundMethod[TypeParameter : Measure](value: TypeParameter) = new TypeParameter(value)
  //  val c = contextBoundMethod[HardDriveSpace](new HardDriveSpace(12))

  //  def g[A](a: TypeParameter)(implicit ev: Measure[TypeParameter]) = something(a)

  //  trait MyMeasure[S : MyMeasure] {
  //    def value:Int
  //    def unit(measure:Int):S
  //    def combine(m:S):S = unit(value+m.value)
  //  }

  //explicit
  //local
  //imported
  //inherited
  //  package
  //  companion object of typeclass
  //  companion object of the type parameter
  //  companion object of super type

}
