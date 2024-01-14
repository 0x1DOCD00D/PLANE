/*
 * Copyright (c) 2023 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package InterestingScalaFeatures

import scala.quoted.{Expr, Quotes, Type}

import scala.annotation.tailrec
import scala.quoted.*

object QuotesTree:

  @main def testQuotesTree =
    import scala.quoted.*
    import scala.quoted.staging.*
    given Compiler = Compiler.make(getClass.getClassLoader)

    def testQuotesTree = run {
      val x = '{1 + 1}
      println(x.show)
      '{ println("Hello World") }
    }
    testQuotesTree