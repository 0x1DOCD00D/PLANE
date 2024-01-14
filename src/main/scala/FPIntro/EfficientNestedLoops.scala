/*
 * Copyright (c) 2023 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package FPIntro

import scala.util.Random

object EfficientNestedLoops:
  val lstOfValsFunc = (n:Int)=>List.range(0, n*n)
  val lstOfProbsFunc = (n:Int)=>List.range(0, n*n).map(_=>(Random.nextDouble)).map(_< 0.3d).zipWithIndex.filter(_._1).map(v=>(v._2/n, v._2%n))

  @main def runEfficientNestedLoops(args: String*): Unit =
    val N = 10
    val nodesLength = N*N
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/FPIntro/EfficientNestedLoops.scala created at time 10:06 AM")
    val lst = lstOfValsFunc(N).zipWithIndex.filter(v=>(v._1%5==0)).map(v=>((v._2/N), v._2%(N)))
    println(lst.mkString(","))

//    (0,0),(5,5),(10,10),(15,15),(20,20),(25,25),(30,30),(35,35),(40,40),(45,45),(50,50),(55,55),(60,60),(65,65),(70,70),(75,75),(80,80),(85,85),(90,90),(95,95)
//    (0,0),(0,5),  (1,1),  (1,6),  (2,2),  (2,7),  (3,3),  (3,8),  (4,4),  (5,0),(5,5),(6,1),(6,6),(7,2),(7,7),(8,3),(8,8),(9,4),(10,0),(10,5)
/*
    println(lst)
    println(probs.mkString(","))
    probs.foreach(println)
*/
