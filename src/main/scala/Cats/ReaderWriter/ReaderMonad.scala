
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.ReaderWriter

/*
 *
 *  Copyright (c) 2021. Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *   
 *   Unless required by applicable law or agreed to in writing, software distributed under
 *   the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *   either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *  
 */

object ReaderMonad:
  import cats.data.Reader
  import cats.instances.*
  import cats.syntax.all.*
  import cats.syntax.eq.*

  trait School
  case class University(name: String) extends School
  case class CommunityCollege(name: String) extends School
  case class Course(id: Int, where: School)

  val univ2Name:Reader[List[School], List[String]] = Reader {
    l => l.foldLeft(List[String]()) {
      (acc,s) => s match
          case University(name) => name :: acc
          case _ => acc
    }
  }

  val univ2courses: Reader[(List[String], List[Course]), Map[String, List[Int]]] = Reader {
    ip => ip._1.foldLeft(Map[String, List[Int]]()) {
      (acc, name) =>
        acc + (name -> ip._2.filter(c=>c.where.isInstanceOf[University] && c.where.asInstanceOf[University].name === name).map(_.id))
    }
  }

  val oneUniv2courses: Reader[(String, List[Course]), Map[String, List[Int]]] = Reader {
    ip => Map(ip._1 -> ip._2.filter(c => c.where.isInstanceOf[University] && c.where.asInstanceOf[University].name === ip._1).map(_.id))
  }


  @main def runMain_ReaderMonad$(): Unit =
    val unames = univ2Name.run(List(University("UIC"), University("UTSA"), CommunityCollege("ACC")))
    println(unames)
    val u2courses = univ2courses.run(unames,
      List(
      Course(1, University("UIC")),
      Course(2, University("UIC")),
      Course(3, University("UTSA")),
      Course(4, CommunityCollege("ACC")),
      Course(5, University("UIC"))
    ))
    println(u2courses)

    val res = for {
      u <- univ2Name.run(List(University("UIC"), University("UTSA"), CommunityCollege("ACC")))
      uc <- oneUniv2courses.run(u,
        List(
          Course(1, University("UIC")),
          Course(2, University("UIC")),
          Course(3, University("UTSA")),
          Course(4, CommunityCollege("ACC")),
          Course(5, University("UIC"))
        ))
    } yield uc
    println(res)

