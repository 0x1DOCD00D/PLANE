////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package ReflectionExperiments

//https://users.scala-lang.org/t/how-to-construct-a-function-call-with-named-arguments-where-the-parameter-names-and-values-are-determined-programmatically-at-runtime/11956/8

object MeetNGreet {
  object G {
    def greet(name: String = "Sam", age: Int = 10): String = s"Hello $name, you're $age years old"
  }
  def main(args: Array[String]): Unit = {
    val m = classOf[G.type].getDeclaredMethods
    m.foreach { method =>
      println(
         s"Method: ${method.getName}, Return Type: ${method.getReturnType}, Parameter Types: ${method.getParameterTypes.mkString(", ")}"
      )
    }
    val greet = m.find(_.getName == "greet").getOrElse(throw new NoSuchMethodException("greet method not found"))
    val p = greet.getParameters
    val params = greet.getParameterTypes
    println(s"Method: ${greet.getName}")
    p.foreach { param =>
      println(
         s"Type Parameter: ${param.getName}, Bounds: ${param.getName}, Annotations: ${param.getAnnotations.mkString(", ")}"
      )
    }
    println(s"Return Type: ${greet.getReturnType}")
  }
}
