/*
 * Copyright (c) 2023 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package InterestingScalaFeatures

object Formatting:
  @main def runFormatting(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/InterestingScalaFeatures/Formatting.scala created at time 1:04 PM")
    val v:Double = 0.123456789
    println(f"The value computed is ${v*0.987}%2.3f")