
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package Cats.Functors

/*
 *
 *  Copyright (c) 2021. Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *   
 *   Unless required by applicable law or agreed to in writing, software distributed under
 *   the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 *   either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *  
 */

/*
* chapter 6 of cats
* SemigroupalExperiments encompasses the notion of composing pairs of contexts.
  Cats provides a cats.syntax.apply module that makes use of
  SemigroupalExperiments and Functor to allow users to sequence functions with multiple arguments.
  Parallel converts types with a Monad instance to a related type with a SemigroupalExperiments instance.
  Applicative extends SemigroupalExperiments and Functor. It provides a way of applying functions to parameters within a context.
  Applicative is the source of the pure method.
  There is only one law for SemigroupalExperiments: the product method must be associative.
* */

object SemigroupalsExperiments:
  import cats.Semigroupal
  import cats.instances.option.*

  case class Cat[T](id:T)

  given Semigroupal[Cat] with {
    override def product[A, B](fa: Cat[A], fb: Cat[B]): Cat[(A, B)] = Cat((fa.id,fb.id))
  }

  case class MyOwnDataType(p1: Int, p2: String, p3: Cat[String])

  @main def runMain_Semigroupals$(): Unit =
//    two contexts are joined
    println(Semigroupal[Option].product(Some(123), Some("abc")))
    println(Semigroupal[Cat].product(Cat("Golda"), Cat("Fima")))
    println(Semigroupal[Option].product(Some(123), Semigroupal[Option].product(Some("abc"),Some("xyz"))))
    println(Semigroupal[Option].product(Semigroupal[Option].product(Some(123), Some("abc")),Some("xyz")))
    println(Semigroupal[Option].product(Some(123), None))
    println(Semigroupal[Option].product(None, Some("abc")))
    println(Semigroupal.tuple3(Option(1), Option(2), Option(Cat("Fima"))))
    println(Semigroupal.tuple3(Option(1), Option(2), Option.empty[Int]))
//    the syntax apply
    import cats.syntax.apply.*
    println((Option(123), Option("abc")).tupled)
    println((Option(123), Option("abc"), Option(true)).tupled)
//    println((Cat("Golda"), Cat("Fima")).tupled)//Could not find an instance of Invariant for Cats.SemigroupalsExperiments.Cat
    val appl1: Option[MyOwnDataType] = (Option(1), Option("p2"), Option(Cat("Golda"))).mapN(MyOwnDataType.apply)
    println(appl1)

    import cats.instances.future.*
    import scala.concurrent.*
    import scala.concurrent.ExecutionContext.Implicits.global
    import scala.concurrent.duration.*

    val futurePair: Future[(String, Int)] = Semigroupal[Future].product(Future("Hello"), Future(123))
    Await.result(futurePair, 1.second)
    println(futurePair)
    val appl2: Future[MyOwnDataType] = (Future(1), Future("p2"), Future(Cat("Golda"))).mapN(MyOwnDataType.apply)
    Await.result(appl2, 2.second)
    println(appl2)

    import cats.instances.list.*
    import cats.syntax.parallel.*
    type ErrorOrList[A] = Either[List[String], A]
    val errStr1: ErrorOrList[Int] = Left(List("error 1"))
    val errStr2: ErrorOrList[Int] = Left(List("error 2"))
    println((errStr1, errStr2).parTupled)