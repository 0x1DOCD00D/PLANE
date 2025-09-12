
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.Functors

import Cats.Functors.Functors.JobRecordF.compJR

/*
* A category is formed by two sorts of objects, the objects of the category, and the morphisms, which relate
* two objects called the source and the target of the morphism. One often says that a morphism is an arrow
* that maps its source to its target. Morphisms can be composed if the target of the first morphism equals the source
* of the second one, and morphism composition has similar properties as function composition
* (associativity and existence of identity morphisms). Morphisms are often some sort of function, but this is not always the case.
* For example, a monoid may be viewed as a category with a single object, whose morphisms are the elements of the monoid.
* The second fundamental concept of category is the concept of a functor, which plays the role of a morphism
*  between two categories {\displaystyle C_{1}}C_{1} and {\displaystyle C_{2}:}{\displaystyle C_{2}:} it maps
*  objects of {\displaystyle C_{1}}C_{1} to objects of {\displaystyle C_{2}}C_{2} and
*  morphisms of {\displaystyle C_{1}}C_{1} to morphisms of {\displaystyle C_{2}}C_{2} in such a way that sources are mapped
*  to sources and targets are mapped to targets (or, in the case of a contravariant functor, sources are mapped to targets and vice-versa).
* A third fundamental concept is a natural transformation that may be viewed as a morphism of functors.
* https://en.wikipedia.org/wiki/Category_theory
* */
object Functors :
  import cats.Functor
  import cats.syntax.all.toFunctorOps
  import com.github.nscala_time.time.Imports.{DateTime, Interval}

  case class JobRecordF[T](company: T, start: DateTime, end: DateTime, position: T)
  object JobRecordF:
    given Functor[JobRecordF] with {
      override def map[A, B](fa: JobRecordF[A])(f: A => B): JobRecordF[B] = JobRecordF(f(fa.company), fa.start, fa.end, f(fa.position))
    }
//    def lift[A, B](f: A => B): F[A] => F[B] = fa => map(fa)(f)
    val liftedUp: JobRecordF[String] => JobRecordF[String] = Functor[JobRecordF].lift(up)
    val liftedLen: JobRecordF[String] => JobRecordF[Int] = Functor[JobRecordF].lift(len)
    val compJR: Functor[[x] =>> JobRecordF[Option[x]]] = Functor[JobRecordF].compose[Option]

  val up: String => String = _.toUpperCase
  val len: String => Int = _.length

  @main def runMainFunctors(): Unit =
    import com.github.nscala_time.time.Implicits.{richDateTime, richInt}
    import com.github.nscala_time.time.Imports.richReadableInstant
    val processStart: DateTime = DateTime.now()
    val processEnd: DateTime = processStart + (1.hours + 10.minutes + 5.seconds)
    val elapsed: Interval = processStart to processEnd
    val newJR = JobRecordF(1, processStart, processEnd, 2).map(_+1)
    println(newJR)
    println(elapsed)
    import Functors.JobRecordF.{liftedLen, liftedUp}
    val upperJR = liftedUp(JobRecordF("ab", processStart, processEnd, "cd"))
    val lenJR = liftedLen(JobRecordF("abc", processStart, processEnd, "cdefg"))
    println(upperJR)
    println(lenJR)
    val incJR = compJR.map(JobRecordF(Some(10), processStart, processEnd, Some(20)))(_ + 1)
    println(incJR)
