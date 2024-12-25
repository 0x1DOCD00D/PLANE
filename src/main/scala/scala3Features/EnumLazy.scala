
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scala3Features

//https://users.scala-lang.org/t/enum-val-extension-init-exception/10465/4

object EnumLazy:
  enum Spam:
    case NoHam
    case Ham
    /*
    Exception in thread "main" java.lang.ExceptionInInitializerError
	at scala3Features.EnumLazy$.toSpam(EnumLazy.scala:21)
	at scala3Features.EnumLazy$.<clinit>(EnumLazy.scala:24)
	at scala3Features.runEnumLazy.main(EnumLazy.scala:25)
    Caused by: scala.MatchError: NoHam (of class scala3Features.EnumLazy$Spam$$anon$1)
	at scala3Features.EnumLazy$Spam.<init>(EnumLazy.scala:17)
	at scala3Features.EnumLazy$Spam$$anon$1.<init>(EnumLazy.scala:15)
	at scala3Features.EnumLazy$Spam$.$new(EnumLazy.scala:15)
	at scala3Features.EnumLazy$Spam$.<clinit>(EnumLazy.scala:14)
    * */
    lazy val x = this match // lazy fixes the error
      case Ham => 0

  extension (s: String)
    def toSpam = s match
      case "HAM" => Spam.Ham
      case _ => Spam.NoHam

  "HAM".toSpam
  @main def runEnumLazy(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/scala3Features/EnumLazy.scala created at time 2:26PM")
    println("BAM".toSpam)
