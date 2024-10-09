////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package TypeFP

import cats.Functor
import cats.instances.list._

object CollectionFunctor:
  def compose[F[_]: Functor, T, S](f: T => S, g: F[T]): F[S] = Functor[F].map(g)(f)

  @main def runCollectionFunctor(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/TypeFP/CollectionFunctor.scala created at time 2:39PM")
    val intToString: Int => String = _.toString
    val listInt: List[Int] = List(1, 2, 3)
    println {
      compose(intToString, listInt)
    }
