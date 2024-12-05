
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package FPIntro

import FPIntro.DynamicClasses.MaliciousUser.maliciousHandler

object DynamicClasses:
  object Library {
    // Secret dynamic class (Symbol) known only to the library
    private val SensitiveErrorTag = Symbol("SensitiveError")

    // Private exception class inaccessible to users
    private class SensitiveException(tag: Symbol, message: String) extends Exception(message) {
      def matches(expectedTag: Symbol): Boolean = tag == expectedTag
    }

    // Function that throws a secret exception
    def libraryFunction(): String = {
      throw new SensitiveException(SensitiveErrorTag, "Sensitive operation failed!")
    }

    // Function for legitimate handlers to recognize the exception
    def handleException(e: Exception): Option[String] = e match {
      case se: SensitiveException if se.matches(SensitiveErrorTag) =>
        Some(se.getMessage) // Handle the sensitive error
      case _ =>
        None // Not a library-handled exception
    }
  }

  object MaliciousUser {
    def maliciousHandler(): String = {
      try {
        Library.libraryFunction()
      } catch {
        case e: Exception =>
          // Attempt to intercept and inspect the exception
          println(s"Malicious handler intercepted: ${e.getMessage}")
          "Exception hijacked!"
      }
    }
  }


  @main def runDynamicClasses(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/FPIntro/DynamicClasses.scala created at time 7:19PM")
    val result = maliciousHandler()
    println(s"Result: $result")
