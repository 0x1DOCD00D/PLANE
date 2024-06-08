/*
 * Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package TypeFP

object DependentTypes2:

  import scala.compiletime.constValue
  import scala.compiletime.ops.int.-

  inline def dependentlyTypedMod2[I <: Int](i: I): Mod2[I] = i match
    case _: 0 => 0
    case _: 1 => 1
    case _ => dependentlyTypedMod2(constValue[I - 2])

  type Mod2[I <: Int] <: Int = I match
    case 0 => 0
    case 1 => 1
    case _ => Mod2[I - 2]

  def main(args: Array[String]): Unit = {
    
  }