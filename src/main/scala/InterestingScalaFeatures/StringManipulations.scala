/*
 * Copyright (c) 2023 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package InterestingScalaFeatures

object StringManipulations:
  @main def runStringMan: Unit =
    val (x, u):(String, Int) = ("x.y.z", 10)
    val `x.y.z`:String = "x.y.z"
//    val (`Gemceas.Generator.debugProgramGeneration`:String, true): String = ("", true)
    val res = "p1.p2.p3".reverse.split("\\.")
    println(res(0).reverse)

    val res1 = "p3".reverse.split("\\.")
    println(res1(0).reverse)

    val res2 = "".reverse.split("\\.")
    println(res2(0).reverse)

    val fullstr = "p1.p2.p3"
    val ind = fullstr.lastIndexOf('.')
    println(fullstr.substring(0, ind))
    println(fullstr.substring(ind+1, fullstr.length))

    val fullstr1 = "p3"
    val ind1 = fullstr1.lastIndexOf('.')
    println(ind1)
    if ind1 < 0 then println(fullstr1) else println(fullstr1.substring(0, ind1))
