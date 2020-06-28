/*
 *
 *  * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package DesignPatterns

import scalaz.Lens

//recommend to watch https://www.youtube.com/watch?v=efv0SQNde5Q&list=PLEDE5BE0C69AF6CCE
object LensPattern extends App {

  //it starts innocently boring, we model a university schedule very primitively
  case class A(b: B, c: C)

  case class B(d: D, attr: String)

  case class C(e: E, f: F)

  case class D(attr: Int, e: E)

  case class E(attr: Int)

  case class F(attr: String)

  //simpler nesting
  case class A1(b: B1)

  case class B1(c: C1)

  case class C1(v: Int)

  //instances
  val simpleInstance = A1(B1(C1(7)))
  val simpleCopiedInstance = simpleInstance.copy(b = simpleInstance.b.copy(c = simpleInstance.b.c.copy(v = 10)))

  //create lenses
  val A12B1 = Lens.lensu[A1, B1]((a1, b1) => a1.copy(b = b1), _.b)
  val B12C1 = Lens.lensu[B1, C1]((b1, c1) => b1.copy(c = c1), _.c)
  val C12Int = Lens.lensu[C1, Int]((c1, value) => c1.copy(v = value), _.v)
  val allTheWayCompose = A12B1 >=> B12C1 >=> C12Int

  println(allTheWayCompose.get(simpleInstance))
  val resetValue = allTheWayCompose.set(simpleInstance, 100)
  println(allTheWayCompose.get(resetValue))

  //then we create instances of these classes
  val instances = A(B(D(3, E(7)), "classB"), C(E(10), F("classF")))
  val plainCopy = instances.copy() //copies the entire object into the new one
  val plainCopyNewC = plainCopy.copy(c = plainCopy.c.copy(e = E(attr = 5000))) //copies the entire object and for the field, c, this field is copied while modifying its subfield e

  //at this point we want to copy the object and modify some of its attributes. We do not want to clone
  //the object schedule, instead we want to modify the values of D and F only. We do not want to use mutability.
  val copiedAndModifiedInstances1 = instances.copy(instances.b.copy(instances.b.d.copy(attr = 100)), instances.c.copy(f = instances.c.f.copy(attr = "newClassF")))

  //we want to copy the object and modify some of its attributes. We do not want to clone
  //the object schedule, instead we want to modify the values of B and E only. We do not want to use mutability.
  val copiedAndModifiedInstances2 = copiedAndModifiedInstances1.copy(instances.b.copy(attr = "newClassB"), copiedAndModifiedInstances1.c.copy(instances.c.e.copy(attr = 1000)))

  val copiedAndModifiedInstances3 = instances.copy(c = instances.c.copy(f = instances.c.f.copy(attr = "newClassF")))

  //Lens a pair of functions: get(input: Container): Element and set(input: Element): Container => Container
  //get is zoom in the container and set is construct a container out of elements

  //  object Lens extends LensInstances with LensFunctions {
  //    def apply[A, B](r: A => Store[B, A]): Lens[A, B] = lens(r) }
  //create lenses
  val A2B = Lens.lensu[A, B]((a, bv) => a.copy(b = bv), _.b)
  val B2D = Lens.lensu[B, D]((b, dv) => b.copy(d = dv), _.d)
  val D2E = Lens.lensu[D, E]((d, ev) => d.copy(e = ev), _.e)
  val E2Int = Lens.lensu[E, Int]((e, value) => e.copy(attr = value), _.attr)
  val allTheWayComposeAgain = A2B >=> B2D >=> D2E >=> E2Int

}
