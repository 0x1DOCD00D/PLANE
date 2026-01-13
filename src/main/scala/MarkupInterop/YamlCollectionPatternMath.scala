////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2026 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package MarkupInterop

object YamlCollectionPatternMath:
  import org.yaml.snakeyaml.Yaml
  import scala.jdk.CollectionConverters.*
  import java.util.{Map as JMap, List as JList}

  // Extract the "sub" value as a String
  // Treat missing or null as empty to mimic <sub></sub>
  def unapply(m: Map[String, Any]): Option[String] =
    m.get("sub") match
      case Some(s: String) => Some(s)
      case Some(null) => Some("")
      case None => None
      case _ => None

  @main def yamlSubPatternMatch(): Unit =
    val yamlText =
      """subs:
        |  - sub: '{"text"}'
        |  - sub: " "
        |  - sub:
        |""".stripMargin

    val rootJava = new Yaml().load(yamlText).asInstanceOf[JMap[String, Any]]
    val subsJava = rootJava.get("subs").asInstanceOf[JList[JMap[String, Any]]]

    val subs: Seq[Map[String, Any]] =
      subsJava.asScala.toSeq.map(_.asScala.toMap)

    def transform(m: Map[String, Any]): String = m match
      case YamlCollectionPatternMath(s) if s.isEmpty => "<empty>"
      case YamlCollectionPatternMath(s) if s.trim.isEmpty => "<whitespace>"
      case YamlCollectionPatternMath(s) => s
      case _ => "non-match"

    // Pattern match over the collection
    println(subs.map(transform))

    // Pattern match the whole collection shape (3 elements)
    subs match
      case Seq(YamlCollectionPatternMath(a), YamlCollectionPatternMath(b), YamlCollectionPatternMath(c)) =>
        println(s"first=$a, second=$b, third=$c")
      case _ =>
        println("shape mismatch")
