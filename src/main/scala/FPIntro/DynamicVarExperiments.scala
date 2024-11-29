
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package FPIntro

object DynamicVarExperiments:

  import scala.util.DynamicVariable

  val context: DynamicVariable[Int] = new DynamicVariable(3)
  val contextF: DynamicVariable[Int=>Int] = new DynamicVariable((x:Int)=>x+context.value)

  def process(): Unit = {
    println(s"Current context value: ${contextF.value(context.value)}")
  }

  @main def runDynamicVarExperiments(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/FPIntro/DynamicVarExperiments.scala created at time 5:24PM")
    process()
    context.withValue(100) {
      process() 
    }

    context.withValue(200) {
      contextF.withValue((x:Int)=>x*context.value){
        process()
      }
    }
    

    process()

