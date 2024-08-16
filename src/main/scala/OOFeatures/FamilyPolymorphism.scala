/*
 *
 *  * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package OOFeatures

trait Family {

  class Father(val name: String) {

    def sleepsWith(mother: Mother): Unit = {
      println(" Father" + name + " sleeps with " + mother.name)
    }
  }

  class Mother(val name: String) {
    def marriedTo(father: Father): Unit = {
      println(name + "  married to " + father.name)
    }
  }

  class Child(val name: String) {
    def loves(m: Mother, f: Father): Unit = {
      println(name + " loves " + f.name + " " + m.name)
    }
  }


}

object CommonerFamily extends Family {

  class CommonerFather(name: String) extends Father(name) {
    override def sleepsWith(mother: Mother): Unit = super.sleepsWith(mother)
  }

  class CommonerMother(name: String) extends Mother(name) {
    override def marriedTo(father: Father): Unit = super.marriedTo(father)
  }

  class child(name: String) extends Child(name) {
    override def loves(m: Mother, f: Father): Unit = super.loves(m, f)
  }

}


object RoyalEnglishFamily extends Family {

  class Duke(name: String) extends Father(name) {
    override def sleepsWith(mother: Mother): Unit = super.sleepsWith(mother)
  }

  class Queen(name: String) extends Mother(name) {
    override def marriedTo(father: Father): Unit = super.marriedTo(father)
  }

  class FutureDuke(name: String) extends Child(name) {
    override def loves(m: Mother, f: Father): Unit = super.loves(m, f)
  }

}

object RunFamily extends App {
  val commonerFather = new CommonerFamily.CommonerFather("Commoner Father")
  val theQueen = new RoyalEnglishFamily.Queen("Victoria")
  val commonerMother = new CommonerFamily.CommonerMother("Commoner Mother")
  commonerFather.sleepsWith(commonerMother)
  //  commonerFather.sleepsWith(theQueen) //  Doesnt compile : scala compiler complains
}