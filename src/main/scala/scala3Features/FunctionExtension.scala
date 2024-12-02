
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scala3Features

object FunctionExtension:
  opaque type FuncType[S,V] = S => Map[V,Any]
  
  extension [S,V](f: FuncType[S,V]) {
    def run(s:S): Map[V, Any] = f(s)
  }
  
  @main def runFunctionExtension(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/scala3Features/FunctionExtension.scala created at time 5:02PM")
    val f: FuncType[String, Int] = (s:String) => Map(s.length -> s.hashCode)
    println(f.run("TestIt"))
