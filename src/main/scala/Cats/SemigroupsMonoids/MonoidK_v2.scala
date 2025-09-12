
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.SemigroupsMonoids

object MonoidK_v2 {

  import cats.MonoidK
  import cats.syntax.all.*

  // Combine a list of F[A] using MonoidK and <+>
  def combineAllK[F[_] : MonoidK, A](xs: List[F[A]]): F[A] =
    xs.foldLeft(MonoidK[F].empty[A])(_ <+> _)

  // First success with Option
  val firstSome: Option[Int] =
    combineAllK[Option, Int](List(None, Some(2), Some(3))) // Some(2)

  // Concatenate results with List
  val all: List[Int] =
    combineAllK[List, Int](List(List(1, 2), List(), List(3))) // List(1,2,3)

  // Foldable#foldK is a convenience when you already have a Foldable container
  val alsoFirstSome: Option[String] = List(None, Some("hi"), Some("ignored")).foldK


  def main(args: Array[String]): Unit = {
    println(s"First Some: $firstSome")
    println(s"All combined List: $all")
    println(s"Also First Some: $alsoFirstSome")
  }
}
