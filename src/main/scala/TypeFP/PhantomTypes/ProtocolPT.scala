
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package TypeFP.PhantomTypes

object ProtocolPT:
  sealed trait ProtocolState

  sealed trait Handshaking extends ProtocolState

  sealed trait Authenticated extends ProtocolState

  sealed trait DataTransfer extends ProtocolState

  sealed trait Terminated extends ProtocolState

  final case class Protocol[State <: ProtocolState] (sessionId: String)

  object Protocol {
    def start(sessionId: String): Protocol[Handshaking] =
      Protocol[Handshaking](sessionId)

    def terminate(): Protocol[Terminated] = {
      println("Terminating during handshake")
      Protocol[Terminated]("terminated-session")
    }

    extension [S <: Handshaking](protocol: Protocol[S]) {
      def authenticate(credentials: String): Protocol[Authenticated] = {
        println(s"Authenticating with: $credentials")
        Protocol[Authenticated](protocol.sessionId)
      }
    }

    extension [S <: Authenticated](protocol: Protocol[S]) {
      def startDataTransfer(): Protocol[DataTransfer] = {
        println("Starting data transfer")
        Protocol[DataTransfer](protocol.sessionId)
      }

      def terminate(): Protocol[Terminated] = {
        println("Terminating after authentication")
        Protocol[Terminated](protocol.sessionId)
      }
    }

    extension [S <: DataTransfer](protocol: Protocol[S]) {
      def sendData(data: String): Protocol[DataTransfer] = {
        println(s"Sending: $data")
        Protocol[DataTransfer](protocol.sessionId)
      }

      def receiveData(): (String, Protocol[DataTransfer]) = {
        val data = "received data"
        println(s"Received: $data")
        (data, Protocol[DataTransfer](protocol.sessionId))
      }

      def finishTransfer(): Protocol[Authenticated] = {
        println("Finishing data transfer")
        Protocol[Authenticated](protocol.sessionId)
      }
    }
  }

  @main def runProtocolPT(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/TypeFP/PhantomTypes/ProtocolPT.scala created at time 12:12PM")
    import Protocol.*
    val protocol = Protocol.start("session-123")
        .authenticate("user:password")
        .startDataTransfer()
        .sendData("Hello, World!")
    val (data, protocol2) = protocol.receiveData()
    println(s"Data received: $data")
    val protocol3 = protocol2.finishTransfer()
        .terminate()
    println(s"Protocol session ${protocol3.sessionId} terminated.")
    // The following lines would not compile:
    // protocol.sendData("This would not compile")
    // protocol.receiveData()
    // protocol.finishTransfer()
    // protocol.terminate()
    // protocol.authenticate("invalid") // Can't authenticate again
    // protocol.startDataTransfer() // Can't start data transfer before authentication
    // protocol.finishTransfer() // Can't finish transfer before starting it
    // protocol.receiveData() // Can't receive data before starting transfer
    // protocol.sendData("data") // Can't send data before starting transfer
    //protocol.terminate() // Can't terminate before authentication

