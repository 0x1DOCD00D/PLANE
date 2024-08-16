/*
 *
 *  * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package InterestingScalaFeatures

//this is a trait that defines the interface for configuring printers
//objects of this trait type are used in the actual printing functionalities
//only interfaces are defined making this trait an abstract type
trait PrinterConfiguration {
  val fonts: List[String]

  def configure(printer: Int): Unit
}

//this is a mixing trait that adds printing to some other functionalities
//we want to send data to a printer defined by its ID and to configure the printer
//but the method configure of the trait PrinterConfiguration is not defined
trait Printing {
  def print(printer: Int, data: String): Unit
}

trait PrintingWithAccess2Configure extends Printing {
  //we tell the compiler that the trait PrintingWithAccess2Configure can access the methods
  //of the trait PrinterConfiguration. But the method configure is not defined, hence this trait
  //can be mixed only with the concrete implementation of this method.
  self: PrinterConfiguration =>
  override def print(printer: Int, data: String) = {
    configure(printer)
    //send data here
    println(data)
  }
}

trait ConfigureAndPrint extends PrinterConfiguration {
  override val fonts: List[String] = List("")

  override def configure(printer: Int): Unit = println(printer)
}

//let us create some types whose functionalities are orthogonal to printing
trait Document {
  self: ConfigureAndPrint with PrintingWithAccess2Configure =>
  val content: String

  def modify(loc: Int, byte: Char) = true

  def outputContent(device: Int) = print(device, content)
}

//we want the trait ConfigureAndPrint to mix with the trait Document to add the printing functionality


object SelfTypeUsage extends App {
  //make sure that we mix in the right traits
  (new ConfigureAndPrint with PrintingWithAccess2Configure).print(1, "data")
  new Document with ConfigureAndPrint with PrintingWithAccess2Configure {
    override val content: String = "DrMark"
  }.outputContent(2)

}
