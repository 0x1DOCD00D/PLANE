/*
 * Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package ApacheMathWorkshop

import com.google.common.reflect.ClassPath

import java.util.stream.Collectors
import scala.jdk.CollectionConverters.*

object RandomGenerators:
  @main def runRandomGenerators(args: String*): Unit =
    val randomGeneratorClass = "Well1024a"
    val pkgName = "org.apache.commons.math3.random"
    val clzz = ClassPath
      .from(ClassLoader.getSystemClassLoader)
      .getAllClasses
      .asScala
      .toList
      .filter(c => c.getPackageName.equalsIgnoreCase(pkgName))
      .filter(c => c.getSimpleName.equalsIgnoreCase(randomGeneratorClass))
      .map(c => c.load())

    clzz.foreach(c => println(s"Class: ${c.getName}"))
    val instances = clzz.map(c => c.getDeclaredConstructor().newInstance())
    instances.foreach(i => println(s"Instance: ${i.getClass.getName}"))
    instances.map(i => i.getClass.getDeclaredMethods.map(m => println(s"Method: ${m.getName}")))
    println(
       "File /Users/drmark/IdeaProjects/PLANE/src/main/scala/ApacheMathWorkshop/RandomGenerators.scala created at time 1:32PM"
    )
