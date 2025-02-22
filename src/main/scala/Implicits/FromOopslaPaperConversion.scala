
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Implicits

object FromOopslaPaperConversion:
  case class Card(n: Int, suit: String) {
    def isInDeck(implicit deck: List[Card]) =
      deck contains this
  }

  import scala.language.implicitConversions

  given intToCard: Conversion[Int, Card] with
    def apply(n: Int): Card = Card(n, "club")

  given deck: List[Card] = List(Card(1, "club"))

  @main def runFromOopslaPaperConversion(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Implicits/FromOopslaPaperConversion.scala created at time 8:20PM")
    println(1.isInDeck)
