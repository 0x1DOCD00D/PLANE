////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package TypeFP

object ListTLP:
  sealed trait AList[+M[_]] {
    type Fun[R]

    def apply0[R, N[X] >: M[X]](f: N[Fun[R]])(using app: Apply[N]): N[R]
  }

  final case class ACons[H, T <: AList[M], +M[_]](head: M[H], tail: T) extends AList[M] {
    def @:[N[X] >: M[X], G](g: N[G]) = ACons(g, this: ACons[H, T, N])

    type Fun[R] = H => tail.Fun[R]

    override def apply0[R, N[X] >: M[X]](f: N[H => tail.Fun[R]])(using app: Apply[N]): N[R] =
      tail.apply0(app.apply(f, head))

    def apply[R, N[X] >: M[X]](f: H => tail.Fun[R])(using p: Pure[N], app: Apply[N]): N[R] =
      apply0(p.pure(f))
  }

  sealed class ANil extends AList[Nothing] {
    def @:[M[_], H](h: M[H]) = ACons(h, this)

    type Fun[R] = R

    def apply0[R, N[X] >: Nothing](n: N[R])(using app: Apply[N]): N[R] = n
  }

  object ANil extends ANil

  // Type class definitions using given
  trait Apply[Z[_]] {
    def apply[A, B](f: Z[A => B], a: Z[A]): Z[B]
  }

  trait Pure[P[_]] {
    def pure[A](a: => A): P[A]
  }

  object Pure {
    given Pure[Option] with {
      def pure[A](a: => A): Option[A] = Some(a)
    }
  }

  object Apply {
    given Apply[Option] with {
      def apply[A, B](f: Option[A => B], a: Option[A]): Option[B] = (f, a) match {
        case (Some(f), Some(a)) => Some(f(a))
        case _                  => None
      }
    }
  }

  @main def runListTLP(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/TypeFP/ListTLP.scala created at time 6:39PM")
    val list: ACons[Int, ACons[Int, ACons[Int, ANil, Some], Some], Some] = Some(1) @: Some(2) @: Some(3) @: ANil

    def sumFunc(x: Int): Int => Int => Int = y => z => x + y + z

    val result = list.apply(sumFunc)
    println(result) // Should print Some(6)
