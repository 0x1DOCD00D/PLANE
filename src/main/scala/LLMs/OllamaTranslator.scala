////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package LLMs

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.*
import io.github.ollama4j.OllamaAPI
import io.github.ollama4j.models.*
import io.github.ollama4j.models.response.OllamaResult
import io.github.ollama4j.utils.Options

import java.util

object OllamaTranslator:
  val host = "http://localhost:11434"
  val model = "llama3:latest"
  val req_timeout: FiniteDuration = 500.seconds

  def generateInstructionPrompt(jlsSentence: String): String = {
    s""" Translate the following Java Language Specification (JLS) sentence to Prolog code,
       | without any preamble or additional explanation:
       | $jlsSentence
       | 1. PROLOG FACTS:
       | [Insert Prolog facts, one per line]
       |
       | 2. PROLOG RULES:
       | [Insert Prolog rules, one per line]
       |
       | 3. KEY TERMS AND RELATIONSHIPS:
       | [List key terms and their relationships, e.g., Term1 -> Relationship -> Term2]
       |
       | Ensure each section is clearly separated and formatted for easy parsing.
       |""".stripMargin
  }

  @main def runOllamaTranslator(args: String*): Unit =
    val ollamaAPI: OllamaAPI = new OllamaAPI(host)
    ollamaAPI.setRequestTimeoutSeconds(req_timeout.toSeconds)

    val jlsStatement = "The scope and shadowing of a class declaration is specified in §6.3 and §6.4.1."

    try {
      val instructionPrompt = generateInstructionPrompt(jlsStatement)
      val result: OllamaResult = ollamaAPI.generate(model, instructionPrompt, false, new Options(new util.HashMap[String, Object]))
      println(s"INPUT: $jlsStatement")
      println(s"OUTPUT: ${result.getResponse}")
      result.getResponse
    } catch {
      case e: Exception =>
        println(s"PROCESS FAILED : ${e.getMessage}")
    }
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/LLMs/OllamaTranslator.scala created at time 12:53PM")
