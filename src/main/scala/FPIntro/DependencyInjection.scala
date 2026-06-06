package FPIntro

object DependencyInjection {
  class Database:
    def connect: Boolean = true
    def getData: String = this.getClass.getTypeName

  class DbSqlServer extends Database
  class DbOracle extends Database

  extension (o: Database)
    def close() = ()

  class MedicalSystem:
    def getPatientData(db: Database): String =
      if db.connect == true then
        db.getData
      else "No data"


  def main(args: Array[String]): Unit = {
    val db = new DbSqlServer
    println((new MedicalSystem).getPatientData(db))
    db.close()
  }
}
