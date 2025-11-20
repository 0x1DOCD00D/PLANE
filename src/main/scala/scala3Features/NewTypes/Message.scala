////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scala3Features.NewTypes

//https://users.scala-lang.org/t/exhaustivity-warning-on-opaque-type/12129/3

object Package:
  opaque type Message = String
  object Message:
    def apply(msg: String): Message = msg

  given scala.reflect.TypeTest[Any, Message] with
    def unapply(msg: Any): Option[msg.type & String] = msg match
      case s: (String & msg.type) => Some(s)
      case _                      => None

@main def runMessage(args: String*): Unit =
  println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/scala3Features/NewTypes/Message.scala created at time 3:42PM")
  import Package.*

  def get: Message | Int = Message("test")
  def getIt: Message | Int = 3

  get match
    case i: Int     => println(i)
    case s: Message => println(s)

  getIt match
    case i: Int => println(i)
    case s: Message => println(s)
