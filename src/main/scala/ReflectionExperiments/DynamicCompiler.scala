////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package ReflectionExperiments
import dotty.tools.dotc.Driver
import java.net.{URL, URLClassLoader}
import java.nio.file.{Files, Path}
import java.io.File

object DynamicCompiler:

  private val driver = Driver()

  def compile(code: String, className: String, outputDir: Path): Boolean =
    val sourceFile = Files.createTempFile(className, ".scala").toFile
    sourceFile.deleteOnExit()
    Files.writeString(sourceFile.toPath, code)

    val args = Array(
       sourceFile.getAbsolutePath,
       "-d",
       outputDir.toAbsolutePath.toString,
       "-classpath",
       System.getProperty("java.class.path")
    )

    val result = driver.process(args)
    !result.hasErrors

  def loadClass(className: String, classDir: Path): Option[Class[?]] =
    val loader = URLClassLoader(Array(classDir.toUri.toURL), getClass.getClassLoader)
    try Some(loader.loadClass(className))
    catch case _: ClassNotFoundException => None

  def main(args: Array[String]): Unit =
    val outDir = Files.createTempDirectory("scala3-compiled")
    val objectName = "DynamicRoot"
    val className = "Root"

    val code =
      s"""
         |case class Address(city: Option[String], country: Option[String], street: Option[String])
         |case class Person(address: Option[Address], age: Option[Long], name: Option[String])
         |case class $className(people: Option[Seq[Person]])
         |object $objectName
         |""".stripMargin

    if compile(code, objectName, outDir) then
      println(s"Compilation succeeded. Output in: $outDir")
      loadClass(objectName, outDir) match
        case Some(objCls) => println(s"Loaded object class: $objCls")
        case None         => println(s"Failed to load object class: $objectName")

      loadClass(className, outDir) match
        case Some(rootCls) => println(s"Loaded case class: $rootCls")
        case None          => println(s"Failed to load case class: $className")
    else
      println("Compilation failed.")
