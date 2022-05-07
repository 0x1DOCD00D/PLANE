/*
 *
 *  * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package Implicits

import scala.collection.mutable.ListBuffer
import scala.util.Random

object ImplicitLayoff extends App {

  //  A human resource termination decision is made by employees of some type, T  or a resource in HR parlance
  //  that decide to fire some external employee, for example, a contractor
  //  who is blamed for some mistakes that resulted in the loss of some business for a company.
  //  The method returns the fired employee, a contractor
  trait HRDecision[T <: FullTimeEmployee, S <: ExternalEmployee] {
    def fireSomeEmployee(emp: T): Option[S]
  }

  // We define a hierarchy of employees
  sealed trait Employee

  class FullTimeEmployee extends Employee

  class ExternalEmployee extends Employee

  class TeamLeaders extends FullTimeEmployee

  class Manager extends FullTimeEmployee

  class Contractor extends ExternalEmployee

  //  Then we provide a function that instantiates the trait for a manager who makes a decision about firing a contractor as part of the layoff
  //  We use the hashcode of the manager object to make a random decision returning a contractor object if s/he is fired and None otherwise
  //val managersFireContractors: HRDecision[Manager, Contractor] = new HRDecision[Manager, Contractor] {
  val managersFireContractors: HRDecision[Manager, Contractor] = (manager: Manager) => if (new Random(manager.hashCode).nextBoolean()) Option(new Contractor)
  else None

  //  The method layoff is where external employees are fired and the list of the fired employees is returned
  //  We create a comprehension iterator where the the function fireThem is invoked to produce a fired employee object
  def layoff[T <: FullTimeEmployee, S <: ExternalEmployee](m: T, fireNEmployees: Int)(fireThem: HRDecision[T, S]): List[S] = {
    var contractors: ListBuffer[S] = ListBuffer()
    for (n <- 0 until fireNEmployees) {
      fireThem.fireSomeEmployee(m) match {
        case Some(c) => contractors += c
        case None =>
      }
    }
    contractors.toList
  }

  //  and voila! - here we go!
  println(layoff(new Manager, 3)(managersFireContractors).length)

  //  We can reduce the number of parameters whose values we should pass by using implicits
  given managersFireContractorsImplicit: HRDecision[Manager, Contractor] with {
    override def fireSomeEmployee(manager: Manager): Option[Contractor] = if (new Random(manager.hashCode).nextBoolean()) Option(new Contractor)
    else None
  }

  //  This is an almost exact replica of the method above except the argument fireThem is declared implicit
  //  it means that the evidence must be found automatically in the searcheable scopes
  def layoffImplicit[T <: FullTimeEmployee, S <: ExternalEmployee](m: T, fireNEmployees: Int)(using fireThem: HRDecision[T, S]): List[S] = {
    var contractors: ListBuffer[S] = ListBuffer()
    for (n <- 0 until fireNEmployees) {
      fireThem.fireSomeEmployee(m) match {
        case Some(c) => contractors += c
        case None =>
      }
    }
    contractors.toList
  }

  //There is no need to specify the implicit argument, since it is located automatically in this scope
  println(layoffImplicit(new Manager, 5).length)
}
