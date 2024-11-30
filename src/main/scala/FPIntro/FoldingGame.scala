
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package FPIntro

object FoldingGame:
  @main def runFoldingGame(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/FPIntro/FoldingGame.scala created at time 12:10PM")
    val numbers = List(1, 2, 3, 4, 5)
    val sumLeft = numbers.foldLeft(0)(_ + _)
    println(s"Sum using foldLeft: $sumLeft")
    val sumRight = numbers.foldRight(0)(_ + _)
    println(s"Sum using foldRight: $sumRight")
    val sumFold = numbers.fold(0)(_ + _)
    println(s"Sum using fold: $sumFold")

    val words = List("Scala", "is", "awesome")
    val sentenceLeft = words.foldLeft("")((acc, word) => acc + word + " ")
    println(s"Sentence using foldLeft: '${sentenceLeft.trim}'")
    val sentenceRight = words.foldRight("")((word, acc) => word + " " + acc)
    println(s"Sentence using foldRight: '${sentenceRight.trim}'")
    val sentenceFold = words.fold("")((acc, word) => acc + word + " ")
    println(s"Sentence using fold: '${sentenceFold.trim}'")

    val reversedLeft = numbers.foldLeft(List.empty[Int])((acc, num) => num :: acc)
    println(s"Reversed using foldLeft: $reversedLeft")
    val reversedRight = numbers.foldRight(List.empty[Int])((num, acc) => acc :+ num)
    println(s"Reversed using foldRight: $reversedRight")

    val numberS = List(3, 1, 4, 1, 5, 9)
    val maxLeft = numberS.foldLeft(Int.MinValue)((acc, num) => acc max num)
    println(s"Maximum using foldLeft: $maxLeft")
    val maxRight = numberS.foldRight(Int.MinValue)((num, acc) => num max acc)
    println(s"Maximum using foldRight: $maxRight")
    val maxFold = numberS.fold(Int.MinValue)((acc, num) => acc max num)
    println(s"Maximum using fold: $maxFold")

    val characters = "abcde"
    val grouped = characters.foldLeft((List.empty[Char], List.empty[Char])) {
      case ((even, odd), char) =>
        if (char.toInt % 2 == 0) (even :+ char, odd) else (even, odd :+ char)
    }
    println(s"Even ASCII: ${grouped._1}, Odd ASCII: ${grouped._2}")

    val fruits = List("apple", "banana", "apple", "orange", "banana", "apple")
    val wordCount = fruits.foldLeft(Map.empty[String, Int]) { (acc, word) =>
      acc.updated(word, acc.getOrElse(word, 0) + 1)
    }
    println(s"Word occurrences: $wordCount")






