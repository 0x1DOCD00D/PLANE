////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package LLMs

import dev.langchain4j.agent.tool.ToolExecutionRequest
import dev.langchain4j.agent.tool.ToolSpecification
import dev.langchain4j.data.message.AiMessage
import dev.langchain4j.data.message.ChatMessage
import dev.langchain4j.data.message.ToolExecutionResultMessage
import dev.langchain4j.data.message.UserMessage
import dev.langchain4j.model.chat.ChatLanguageModel
import dev.langchain4j.model.output.Response
import dev.langchain4j.agent.tool.JsonSchemaProperty.*
import dev.langchain4j.data.message.SystemMessage.systemMessage
import dev.langchain4j.data.message.ToolExecutionResultMessage.from
import dev.langchain4j.data.message.UserMessage.userMessage
import io.github.amithkoujalgi.ollama4j.core.OllamaAPI

object ModelStuff:
  val ollamaAPI = new OllamaAPI("http://localhost:11434")
  @main def runModelStuff(args: String*): Unit =
    println(
       "File /Users/drmark/IdeaProjects/PLANE/src/main/scala/LLMs/ModelStuff.scala created at time 2:07PM"
    )
    ollamaAPI.listModels().toArray.foreach(println)
