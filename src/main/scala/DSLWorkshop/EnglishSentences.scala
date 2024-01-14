/*
 * Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package DSLWorkshop

/*
* Basic: subject, verb, object, prepositional phrase
* Compound: Basic, conjunction, Basic
* Complex: Basic or Compound, comma, Basic or Compound
* */
sealed trait GrammarComponent
case object Subject extends GrammarComponent:
  infix def verb(o: ObjectOfAction.type): ObjectOfAction.type = o
case object ObjectOfAction extends GrammarComponent:
  infix def prepositionalPhrase(p: String): ObjectOfAction.type = ObjectOfAction

  infix def and(p: Subject.type): Subject.type = p

object EnglishSentences:
  def main(args: Array[String]): Unit =
    Subject verb ObjectOfAction prepositionalPhrase "to the store" and Subject verb ObjectOfAction prepositionalPhrase "to the bank"
    println("Hello, world!")