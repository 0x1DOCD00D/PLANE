////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package InterestingScalaFeatures

import InterestingScalaFeatures.SelfOfThis.c

object SelfOfThis:
  class Container():
    val self: this.type = this

    class Contained() {
      def container: self.type = self
      def me: self.Contained = this

      def executeBlock(block: (context: self.type) => Unit): Unit = {
        block(self)
//        path depends on the type of self, not the type of this
//        block(Container().self)
      }

    }

    def item = self.Contained()

    def getItem(n: self.Contained): Unit = println("getItem")
  val c = Container()
  val i: c.Contained = c.item
  c.getItem(i)
  c.item.container

  def method(p: Container#Contained): Any = p.container.getItem(p.me)

  @main def runSelfOfThis(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/InterestingScalaFeatures/SelfOfThis.scala created at time 10:41AM")
    c.Contained().executeBlock { context =>
      println(s"Executing block with context: $context")
    }
