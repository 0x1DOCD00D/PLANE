
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scala3Features

import scala3Features.StateWithOpaqueness.Input.{Coin, Turn}

//https://github.com/fpinscala/fpinscala/blob/second-edition/src/main/scala/fpinscala/answers/state/State.scala

object StateWithOpaqueness:
  opaque type State[S, +A] = S => (A, S)

  def unit[S, A](a: A): State[S, A] =
    s => (a, s)

  extension [S, A](underlying: State[S, A]) {
    def run(s: S): (A, S) = underlying(s)

    def map[B](f: A => B): State[S, B] =
      flatMap(a => unit(f(a)))

    def map2[B, C](sb: State[S, B])(f: (A, B) => C): State[S, C] =
      for
        a <- underlying
        b <- sb
      yield f(a, b)

    def flatMap[B](f: A => State[S, B]): State[S, B] =
      s =>
        val (a, s1) = underlying(s)
        f(a)(s1)
  }

  object State:
    def sequence[S, A](actions: List[State[S, A]]): State[S, List[A]] =
      actions.foldRight(unit[S, List[A]](Nil))((f, acc) => f.map2(acc)(_ :: _))

    def traverse[S, A, B](as: List[A])(f: A => State[S, B]): State[S, List[B]] =
      as.foldRight(unit[S, List[B]](Nil))((a, acc) => f(a).map2(acc)(_ :: _))

    def apply[S, A](f: S => (A, S)): State[S, A] = f

    def get[S]: State[S, S] = s => (s, s)

    def set[S](s: S): State[S, Unit] = _ => ((), s)

    def modify[S](f: S => S): State[S, Unit] =
      for
        s <- get // Gets the current state and assigns it to `s`.
        _: Unit <- set(f(s)) // Sets the new state to `f` applied to `s`.
      yield ()

  enum Input:
    case Coin, Turn

  case class Machine(locked: Boolean, candies: Int, coins: Int)

  object Candy:
    def simulateMachine(inputs: List[Input]): State[Machine, (Int, Int)] =
      for
        _ <- State.traverse(inputs)(i => State.modify(update(i)))
        s <- State.get
      yield (s.coins, s.candies)

    val update: Input => Machine => Machine = (i: Input) => (s: Machine) =>
      (i, s) match
        case (_, Machine(_, 0, _)) => s
        case (Input.Coin, Machine(false, _, _)) => s
        case (Input.Turn, Machine(true, _, _)) => s
        case (Input.Coin, Machine(true, candy, coin)) =>
          Machine(false, candy, coin + 1)
        case (Input.Turn, Machine(false, candy, coin)) =>
          Machine(true, candy - 1, coin)

  @main def runStateWithOpaqueness(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/scala3Features/StateWithOpaqueness.scala created at time 2:21PM")
    val r1 = Candy.update(Coin)(Machine(true, 3, 1))
    println(r1)
    println(Candy.update(Turn)(r1))

