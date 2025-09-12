
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.StateManipulations

object EnvSymbolTable:
  import cats.data.State
  import cats.syntax.all.*

  // -----------------------------
  // State model
  // S is our “world” (counter + symbol table). A is the produced value.
  // State[S, A] is a wrapper over a function S => (S, A).
  // -----------------------------
  final case class SymState(counter: Long, table: Map[String, String])

  type ST[A] = State[SymState, A]

  // fresh: increments counter, returns a unique internal name like v42.
  def fresh(prefix: String = "v"): ST[String] =
    State { s =>
      val n = s"$prefix${s.counter}"
      val s1 = s.copy(counter = s.counter + 1)
      (s1, n)
    }

  // intern: assign a stable unique name to a user variable, idempotently.
  def intern(user: String, prefix: String = "v"): ST[String] =
    for {
      existing <- State.inspect[SymState, Option[String]](_.table.get(user))
      name <- existing match {
        case Some(n) => State.pure[SymState, String](n)
        case None =>
          for {
            n <- fresh(prefix)
            _ <- State.modify[SymState](s => s.copy(table = s.table + (user -> n)))
          } yield n
      }
    } yield name

  // Rename a list of user vars into unique internal names and return mapping.
  def internAll(users: List[String], prefix: String = "v"): ST[Map[String, String]] =
  /*
    users: List[String]
    intern(u, prefix): State[SymState, String]
    A state action that returns the fresh internal name for u.

    .map(u -> _): turns the returned String into a pair (u, name) inside the same state.
    It is shorthand for .map(name => (u -> name)), i.e. .map(name => (u, name)).
    Result type: State[SymState, (String, String)].

    users.traverse(f): sequences the list of stateful actions left-to-right, threading SymState.
    Result type: State[SymState, List[(String, String)]].

    .map(_.toMap): converts the List[(String, String)] inside the state into a Map[String, String].
    Final type: State[SymState, Map[String, String]].
  * */
    users.traverse(u => intern(u, prefix).map(u -> _)).map(_.toMap)
    /*
      State[S, A] is just S => (S, A). traverse on a List runs each intern(u, prefix) in order,
      feeding the new SymState forward and collecting the (u, freshName) pairs.
    * */

  // -----------------------------
  // “Behind the scenes” view (desugared):
  // - flatMap on State literally runs run(s0) to get (s1, a)
  //   then runs the next State with s1.
  // - map just transforms the A and leaves S alone.
  // -----------------------------
  def internAll_desugared(users: List[String]): SymState => (SymState, Map[String, String]) =
    s0 => internAll(users).run(s0).value

  @main def runEnvSymbolTable(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/Cats/StateManipulations/EnvSymbolTable.scala created at time 12:49PM")
    val users = List("x", "y", "x", "z", "x")
    val s0 = SymState(counter = 1L, table = Map.empty)

    val (s1, mapping) = internAll(users, prefix = "t").run(s0).value

    println(s"Mapping  => $mapping") // user -> fresh
    println(s"Counter  => ${s1.counter}") // advanced deterministically
    println(s"SymTable => ${s1.table}") // stable interning

