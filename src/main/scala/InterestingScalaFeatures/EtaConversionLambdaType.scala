
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package InterestingScalaFeatures

import scala.io.StdIn.readLine

//https://docs.scala-lang.org/sips/polymorphic-eta-expansion.html

object EtaConversionLambdaType:
  def uf2[A]: A = readLine("prompt> ").asInstanceOf
  val vuf2: [B] => B ?=> B = uf2
  @main def runEtaConversionLambdaType(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/InterestingScalaFeatures/EtaConversionLambdaType.scala created at time 5:33PM")
    println("vuf2: " + vuf2.toString())
