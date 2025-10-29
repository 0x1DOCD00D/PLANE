////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package ReflectionExperiments

//https://users.scala-lang.org/t/inferred-type-issue/12090/8

object TypeImplicitClasstagParms:

  import scala.reflect.ClassTag

  extension [X](seq: Seq[X])(using xTag: ClassTag[X])
    def contains_strict[Y <: X](y: Y): Boolean =
      println(s"X = ${xTag.runtimeClass.getName}, Y = ${y.getClass.getName}")
      seq.contains(y)

  @main def runTypeImplicitClasstagParms(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/ReflectionExperiments/TypeImplicitClasstagParms.scala created at time 7:20PM")
    val res = Seq(3.1415926).contains_strict(1)
    println(res)
    //Seq(1).contains_strict(3.1415926)

