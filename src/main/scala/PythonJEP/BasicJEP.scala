////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package PythonJEP

object BasicJEP:
  @main def runBasicJEP(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/PythonJEP/BasicJEP.scala created at time 12:39PM")
    println(">>> Running embedded Python code...")
    println("java.library.path=" + System.getProperty("java.library.path"))
    println("jep.library.path="  + System.getProperty("jep.library.path"))
    println("python.home="       + System.getProperty("python.home"))
    println("jep.include.path="  + System.getProperty("jep.include.path"))

    val si = new jep.SharedInterpreter()
    try
      // install a simple Python stdout/stderr capturer
      si.exec(
        """import sys, io
          |if not hasattr(sys, "_scala_out"):
          |    sys._scala_out = io.StringIO()
          |    sys._scala_err = io.StringIO()
          |    sys._old_stdout, sys._old_stderr = sys.stdout, sys.stderr
          |    class _ScalaWriter:
          |        def __init__(self, buf): self.buf = buf
          |        def write(self, s): self.buf.write(s)
          |        def flush(self): pass
          |    sys.stdout = _ScalaWriter(sys._scala_out)
          |    sys.stderr = _ScalaWriter(sys._scala_err)
          |""".stripMargin)

      // your Python code
      si.exec("print('hello from Python stdout')")
      si.exec("import sys; sys.stderr.write('oops on stderr\\n')")

      // flush and read captured output
      val out = si.getValue("sys._scala_out.getvalue()", classOf[String])
      val err = si.getValue("sys._scala_err.getvalue()", classOf[String])

      // show in Scala console
      if out.nonEmpty then println("[PY-STDOUT]\n" + out.trim)
      if err.nonEmpty then Console.err.println("[PY-STDERR]\n" + err.trim)
    finally si.close()