////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package AST

import AST.AstPrintMacro

import scala.quoted.*

object AstPrint:
  import AstPrintMacro.*
  inline def printAST(inline expr: Any): Unit = ${ printASTImpl('expr) }
  inline def getAST(inline expr: Any): String = ${ getASTImpl('expr) }

  @main def runAstPrint(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/scala3Features/AstPrint.scala created at time 8:40AM")
    val ts = getAST {
      val x = 42
      val y = x + 1
      y * 2
    }
    println(ts)
    System.out.flush()
