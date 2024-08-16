package Cats

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
