
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package FPIntro

object SequenceFunction:
  def sequence[A](as: List[Option[A]]): Option[List[A]] =
    if as.count(_.isEmpty) == as.length then None
    else Some(as.filter(_.isDefined).map(_.get))

  def traverse[A, B](as: List[A])(f: A => Option[B]): Option[List[B]] = sequence(as.map(f(_)))
    
  @main def runSequenceFunction(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/FPIntro/SequenceFunction.scala created at time 2:07PM")
    val lst = List(None, Option("a"), None, None, Option(2.3f), Option((1, "b")), Option(5))
    val lst1 = List(None, "a", None, None, 2.3f, Option((1, "b")), 5)
    println(sequence(lst))
    val f: Any => Option[Int] = (s:Any)=>{
      try
        Some(s.toString.toInt)
      catch
        case  _ => None
    } 
    println(traverse(lst1)(f))
