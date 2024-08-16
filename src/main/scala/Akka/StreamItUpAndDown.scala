/*
 * Copyright (c) 2022 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package Akka
import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.util.Timeout

object StreamItUpAndDown:
  given ActorSystem = ActorSystem("StreamItUpAndDown")
  @main def runStreamItUpAndDown(args: String*): Unit =
    import scala.concurrent.duration.*
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Akka/StreamItUpAndDown.scala created at time 10:05 AM")
    given Timeout = 5.seconds

    val shortAndLongNames = List("Shorty", "Short", "LongName1", "Ouch", "LongName2", "Mark Grechanik")
    val source = Source(shortAndLongNames)
    val flow1 = Flow[String].filter(name => name.length > 8)
    val flow2 = Flow[String].take(2)
    val sink = Sink.foreach[String](println)

    source.via(flow1).via(flow2).to(sink).run()
    source.filter(_.length > 8).take(2).runForeach(println)

    val intSource = Source(1 to 1000)
    val intFlow = Flow[Int].map(_ + 1)
    val intFlow2 = Flow[Int].map(_ * 10)
    val intSink = Sink.foreach[Int](println)
    intSource.via(intFlow).async.via(intFlow2).async.to(intSink).run()//async can be omitted to run the flow in the same actor

    summon[ActorSystem].terminate()
