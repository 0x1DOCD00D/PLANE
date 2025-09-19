
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package TypeFP.PhantomTypes

object DatabasePT:
  // Database Connection with phantom types for tracking connection state
  sealed trait ConnectionState

  sealed trait Connected extends ConnectionState

  sealed trait Disconnected extends ConnectionState

  sealed trait InTransaction extends ConnectionState

  final case class DatabaseConnection[State <: ConnectionState] private(
                                                                         connectionString: String,
                                                                         isConnected: Boolean,
                                                                         inTransaction: Boolean
                                                                       )

  object DatabaseConnection {
    def create(connectionString: String): DatabaseConnection[Disconnected] =
      DatabaseConnection[Disconnected](connectionString, false, false)

    extension [S <: Disconnected](conn: DatabaseConnection[S])
      def connect(): DatabaseConnection[Connected] =
        conn.copy(isConnected = true)

    extension [S <: Connected](conn: DatabaseConnection[S]) {
      def disconnect(): DatabaseConnection[Disconnected] =
        conn.copy(isConnected = false)

      def beginTransaction(): DatabaseConnection[InTransaction] =
        conn.copy(inTransaction = true)

      def query(sql: String): List[String] = {
        println(s"Executing query: $sql")
        List("result1", "result2")
      }
    }

    extension [S <: InTransaction](conn: DatabaseConnection[S]) {
      def commit(): DatabaseConnection[Connected] =
        conn.copy(inTransaction = false)

      def rollback(): DatabaseConnection[Connected] =
        conn.copy(inTransaction = false)

      def execute(sql: String): Unit = {
        println(s"Executing in transaction: $sql")
      }
    }
  }

  @main def runDatabasePT(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/TypeFP/PhantomTypes/DatabasePT.scala created at time 11:49AM")
    val disconnectedConn = DatabaseConnection.create("db://localhost:5432/mydb")
    val connectedConn = disconnectedConn.connect()
    val results = connectedConn.query("SELECT * FROM users")
    println(s"Query results: $results")
    val inTransactionConn = connectedConn.beginTransaction()
    inTransactionConn.execute("UPDATE users SET name = 'Alice' WHERE id = 1")
    val afterCommitConn = inTransactionConn.commit()
    afterCommitConn.disconnect()
    println("Disconnected from database.")
    // The following lines would not compile due to phantom type constraints:
    // disconnectedConn.query("SELECT * FROM users")
    // connectedConn.execute("UPDATE users SET name = 'Alice' WHERE id = 1")
    // inTransactionConn.connect()
    // afterCommitConn.beginTransaction()
