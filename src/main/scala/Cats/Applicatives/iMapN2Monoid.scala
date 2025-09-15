
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.Applicatives

import cats.implicits.catsSyntaxSemigroup

object iMapN2Monoid:
  import cats.Monoid
  import cats.syntax.apply.*
  import cats.instances.string.*
  import cats.instances.int.*
  import cats.instances.list.*

  final case class Report(title: String, total: Int, tags: List[String])

  /*
  Scala generates:
    a companion object Report that contains
      def apply(title: String, total: Int, tags: List[String]): Report
      def unapply(r: Report): Option[(String, Int, List[String])]
      def copy(...), and a few more utilities

    (Monoid[String], Monoid[Int], Monoid[List[String]]).imapN(Report.apply)(r => (r.title, r.total, r.tags))
    the left side is a product of field Monoids. Each type (String, Int, List[String]) has a Monoid instance in scope.

    imapN first builds a Monoid[(String, Int, List[String])], then needs a two-way mapping between that tuple and Report.

    Report.apply is the forward map of type (String, Int, List[String]) => Report. Thanks to eta-expansion, the method is used as a function value.

    r => (r.title, r.total, r.tags) is the backward map of type Report => (String, Int, List[String]).

    (Monoid[String], Monoid[Int], Monoid[List[String]]).imapN(Report(_, _, _))(r => (r.title, r.total, r.tags))
    
    // Using unapply (safe for case classes because unapply always returns Some)
    (Monoid[String], Monoid[Int], Monoid[List[String]]).imapN(Report.apply)(Report.unapply(_).get)
    
    To see the two steps explicitly, you can write it as “product then invariant map.
    
    import cats.{Invariant, Monoid}
    import cats.syntax.apply.*
    import cats.instances.string.*, cats.instances.int.*, cats.instances.list.*
    
    val tupleMonoid: Monoid[(String, Int, List[String])] = (Monoid[String], Monoid[Int], Monoid[List[String]]).tupled
    
    val reportMonoid: Monoid[Report] =
      Invariant[Monoid].imap(tupleMonoid)(Report.apply)(r => (r.title, r.total, r.tags))
    
    Report.apply comes from the case class companion and is just the constructor as a function.
    imapN needs both directions because Monoid is invariant. You give it the constructor for the forward direction and a projector for the reverse direction.
    The “triple of Monoids” is not saying the fields are Monoids, it says there are Monoid instances for the field types, which imapN uses to derive a Monoid[Report].
  * 
  * */
  given reportMonoid: Monoid[Report] =
    (Monoid[String], Monoid[Int], Monoid[List[String]])
      .imapN(Report.apply)(r => (r.title, r.total, r.tags))

  @main def runiMapN2Monoid(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Cats/Applicatives/iMapN2Monoid.scala created at time 2:24PM")
    val a = Report("Daily: ", 10, List("sales"))
    val b = Report("Summary", 5, List("ops"))
    val c = a |+| b // Report("Daily: Summary", 15, List("sales", "ops"))
    println(c)

