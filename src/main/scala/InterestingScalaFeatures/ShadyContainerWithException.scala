/*
 * Copyright (c) 2023 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package InterestingScalaFeatures

import scala.reflect.ClassTag

object ShadyContainerWithException :

  trait Container[T <: Container[T]] extends Ordered[T] {
    def getType[U: ClassTag](obj: U): ClassTag[U] = summon[ClassTag[U]]
  }

  class MyContainer extends Container[MyContainer] {
    override def compare(that: MyContainer): Int = {
      if (getType(that) != getType(this)) throw new Exception("type mismatch")
      0
    }
  }

  class ShadyContainer extends Container[MyContainer] {
    override def compare(that: MyContainer): Int = {
      if (getType(that) != getType(this)) throw new Exception("type mismatch")
      0
    }
  }

  def main(args: Array[String]): Unit = {
    new MyContainer().compare(new MyContainer)
    new ShadyContainer().compare(new MyContainer)

  }
