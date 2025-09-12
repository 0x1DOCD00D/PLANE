
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.ReaderWriter

import cats.data.Reader

object ReaderComposition:
// Int (uin) => Student => AcademicRecord => GPA => Scholarship
  def Uin2StudentName(uin:Int):String = if uin %2 == 0 then "Mark" else "Golda"
  case class Student(name: String) {
    def academicRecord(): AcademicRecord = AcademicRecord(name.length.toFloat)
  }
  case class AcademicRecord( gpa: Float):
    def obtainGPA: Float = gpa

  case class Scholarship(student: Option[Student])
  def getScholarship(gpa:Float): Boolean = if gpa > 3.8 then true else false

  @main def runReaderComposition(): Unit =
    val readUin2Student: Reader[Int, Student] = Reader(uin => Student(Uin2StudentName(uin)))
    val readUin2AcadRecord: Reader[Int, AcademicRecord] = readUin2Student.map(student => student.academicRecord())
    val readUin2ScholarshipYesNo: Reader[Int, Boolean] = readUin2AcadRecord.map(academicRecord => getScholarship(academicRecord.obtainGPA))

    val readUin2Scholarship1: Reader[Int, Scholarship] = Reader((uin:Int) => Student(Uin2StudentName(uin))).
      map(student=>student.academicRecord()).
      map(ar => Scholarship(if getScholarship(ar.obtainGPA) then Option(readUin2Student.run(123)) else None))

    val program = for {
      student <- readUin2Student
      academicRecord <- readUin2AcadRecord
      yesNo <- readUin2ScholarshipYesNo
      sc = Scholarship(if yesNo then Option(student) else None)
    } yield sc

/*
    println(readUin2Student.run(123))
    println(readUin2AcadRecord.run(123))
    println(readUin2Scholarship1.run(123))
*/
    println(program.run(123))
