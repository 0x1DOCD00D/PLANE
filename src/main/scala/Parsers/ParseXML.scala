/*
 *
 *  * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package Parsers

import java.nio.file.{Files, Paths}

import scala.xml.{PrettyPrinter, XML}

object ParseXML extends App {
  System.setProperty("entityExpansionLimit", String.valueOf(Integer.MAX_VALUE))
  //  val xml = XML.loadFile("/home/drmark/Downloads/dblp.xml")
  val xml = Files.readAllBytes(Paths.get("/home/drmark/Downloads/dblp.xml"))
  //Source.fromFile("/home/drmark/Downloads/dblp.xml")..getLines.toString.mkString("")
  //  val publication = XML.loadString("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n<!DOCTYPE dblp SYSTEM \""+getClass.getClassLoader.getResource("/home/drmark/Downloads/dblp.dtd").toURI.toString+"\">" + xml)
  val publication = XML.loadString(xml.toString)
  val pp = new PrettyPrinter(24, 4)
}
