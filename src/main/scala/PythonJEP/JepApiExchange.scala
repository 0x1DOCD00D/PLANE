////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package PythonJEP

import jep.*
import scala.jdk.CollectionConverters.*

object JepApiExchange:
  @main def runJepApiExchange(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/PythonJEP/JepApiExchange.scala created at time 3:24PM")
    println(">>> Running embedded Python code...")
    println("java.library.path=" + System.getProperty("java.library.path"))
    println("jep.library.path=" + System.getProperty("jep.library.path"))
    println("python.home=" + System.getProperty("python.home"))
    println("jep.include.path=" + System.getProperty("jep.include.path"))

    val py = new SharedInterpreter()
    try
      py.exec(
        """import urllib.request, json
          |def fetch_titles_and_status(url):
          |    req = urllib.request.Request(url, headers={"User-Agent": "Jep-Scala-Demo"})
          |    with urllib.request.urlopen(req, timeout=10) as resp:
          |        raw = resp.read().decode("utf-8")
          |    data = json.loads(raw)
          |    # Example API returns a list of todo objects; adapt this to your API
          |    titles = [item["title"] for item in data]
          |    message = f"Fetched {len(titles)} items successfully"
          |    return titles, message
          |""".stripMargin)

      val apiUrl = "https://jsonplaceholder.typicode.com/todos?userId=1"
      py.set("api_url", apiUrl)
      py.exec("titles, status_msg = fetch_titles_and_status(api_url)")

      val titlesJava = py.getValue("titles", classOf[java.util.List[String]])
      val status     = py.getValue("status_msg", classOf[String])

      val titles: List[String] = titlesJava.asScala.toList

      println(s"[STATUS] $status")
      println("[TITLES]")
      titles.take(5).zipWithIndex.foreach { case (t,i) => println(f"  ${i+1}%2d. $t") }
      if (titles.size > 5) println(s"  ... and ${titles.size - 5} more")
    finally
      py.close()