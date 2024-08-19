////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package LLMs

import org.nd4j.linalg.api.buffer.DataType
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j

object DLExperiments:
  @main def runDLExperiments(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/LLMs/DLExperiments.scala created at time 8:34PM")
    val encodedLines = Array[Byte](1, 2, 3, 4, 5)
    var tensor = Nd4j.create(encodedLines.map(_.toFloat), Array[Long](encodedLines.length), 'c')
    val resTensor = tensor.castTo(DataType.INT8)
    println(resTensor.length())
