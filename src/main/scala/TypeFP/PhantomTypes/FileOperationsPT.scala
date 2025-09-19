
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package TypeFP.PhantomTypes

object FileOperationsPT:
  sealed trait FilePermission

  sealed trait ReadOnly extends FilePermission

  sealed trait WriteOnly extends FilePermission

  sealed trait ReadWrite extends ReadOnly with WriteOnly

  final case class FileHandle[Perm <: FilePermission] private(path: String)

  object FileHandle {
    def openReadOnly(path: String): FileHandle[ReadOnly] =
      FileHandle[ReadOnly](path)

    def openWriteOnly(path: String): FileHandle[WriteOnly] =
      FileHandle[WriteOnly](path)

    def openReadWrite(path: String): FileHandle[ReadWrite] =
      FileHandle[ReadWrite](path)

    // Only readable files can be read from
    extension [P <: ReadOnly](handle: FileHandle[P]) {
      def read(): String = {
        println(s"Reading from ${handle.path}")
        "file contents"
      }
    }

    extension [P <: WriteOnly](handle: FileHandle[P]) {
      def write(content: String): Unit = {
        println(s"Writing to ${handle.path}: $content")
      }
    }
  }

  @main def runFileOperationsPT(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/TypeFP/PhantomTypes/FileOperationsPT.scala created at time 12:15PM")
    import FileHandle.*
    val writeOnlyFile = FileHandle.openWriteOnly("/path/to/output.txt")
    writeOnlyFile.write("Hello, World!")
    //writeOnlyFile.read() // This would not compile!

    val readWriteFile = FileHandle.openReadWrite("/path/to/data.txt")
    readWriteFile.write("Data")
    val data = readWriteFile.read()
    println(s"Read back: $data")

    val readOnlyFile = FileHandle.openReadOnly("/path/to/input.txt")
    val content = readOnlyFile.read()
    println(s"Read content: $content")
    //readOnlyFile.write("Trying to write") // This would not compile!
// The following lines would not compile:
//    val invalidRead: FileHandle[ReadOnly] = writeOnlyFile
//    val invalidWrite: FileHandle[WriteOnly] = readOnlyFile
// val invalidReadWrite: FileHandle[ReadWrite] = readOnlyFile
// val invalidReadWrite2: FileHandle[ReadWrite] = writeOnlyFile
// readOnlyFile.write("Attempt to write") // Error: write not available
// writeOnlyFile.read() // Error: read not available

