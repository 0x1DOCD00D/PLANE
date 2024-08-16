/*
 *
 *  * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package Variance

import Variance.HierarchyOfScholars._

/*
* The purpose of these examples is to explain how to use variance in defining and using abstractions for modeling and
* creating software applications. Liskov's substitutivity principle is clear when applied to objects whose types are
* organized in a subtyping hierarchy and the objects of some subtype can be substituted in place where the object of its
* supertype can be used. Doing so has a clear intuition based on the rule that the set of operations of the supertype is
* the subset of the set of the operations of its subtype. Hence, no ambiguity exists.
* Things change when we introduce types that we call containers - they contain objects whose types defined by some type hierarchy.
* Will the subtyping relations among the containers follow the subtyping relation between the types that they contain,
* i.e., being covariant or it would go the opposite way, i.e., being contravariant? What if there is no relation at all, i.e., invariant,
* like Bank[ArmedService] vs Bank[GovernmentEmployee] even though an Army Serviceman is a subtype of the Government Employee?
*
* When we describe container types we will mean both parameterized types and basic arrays. An array of Integers may be a subarray
* of an array of Objects. When applying Liskov's substitutivity principle, we can see that when an array of Integers becomes
* an array of objects, a string object can be inserted in this array since the type String is a subtype of the type Object.
* Hence, the main problem with subtyping container types is how to prevent violating typing rules for the content of these
* containers, that is if a container is declared to contain object of some type, T it should not store objects of some other different type, S
* even if T and S have some common supertype, i.e., T <: O /\ S <: O. The idea of defining variances and typing rules is to prevent
* this kind of type violations.
*
* As containers we will use the following types: ScholasticInterestGroup and RulesOfConduct.
* */

object ScholarsVariances extends App {

  abstract class ScholasticInterestGroup[+T] {
    val people: List[T]

    def get(index: Int): Option[T] = if (index < people.length) Some(people(index)) else None

    /*
        The compiler issues an error that the covariant type T occurs in the contravariant position.
        def add(member:T):List[T] = member :: people
        The problem is in the application of Liskov's substitutivity principle to this container.
        val sigScholar: ScholasticInterestGroup[Scholar] = new ScholasticInterestGroup[Student]
        We can add objects of the subtype of Scholar to the container using the variable sigScholar as in
        sigScholar.add(new Professor) or
        sigScholar.add(new TeachingTrack)
        It means that we created a SIG for students and by assigning it to the variable of the supertype we
        can add professors to the SIG for students! This is not correct, since the clients of this container object will
        obtain references to its members based on the type contract that these members are students. This operation should
        not be allowed by the compiler's type checker.
                ScholasticInterestGroup[Scholar]
                /                             \
        ScholasticInterestGroup[Junior]       ScholasticInterestGroup[Tenured]
        If it is possible to cast containers by going up the type hierarchy to its root, then a container of Juniors can
        be assigned to a variable that is a container of Scholars. Since all subtypes are valid for the container of Scholars,
        it is possible to insert an object of Tenured prof into the container of Juniors thereby invalidating the contract that
        the creator of the container of Juniors imposed on its clients.
    */
  }

  //  Let us provide some API calls for clients to operate on SIGs
  def isSIGImportant[Professor](sig: ScholasticInterestGroup[Professor]): Boolean = if (sig.people.length > 5) true else false

  println(isSIGImportant(new ScholasticInterestGroup[Professor] {
    override val people: List[Professor] = List(new Professor, new Untenured, new Tenured)
  }))

  println(isSIGImportant(new ScholasticInterestGroup[TeachingTrack] {
    override val people: List[TeachingTrack] = List(new TeachingTrack, new TeachingTrack, new TeachingTrack)
  }))

  abstract class ScholasticInterestGroup_Add_Allowed[+T] {
    val people: List[T]

    def get(index: Int): Option[T] = if (index < people.length) Some(people(index)) else None

    def add[S >: T](member: S): List[S] = member :: people

    /*
      We parameterize the method add by adding an additional type, S that is the supertype of T.

    */

  }

  val sigScholar: ScholasticInterestGroup_Add_Allowed[Student] = new ScholasticInterestGroup_Add_Allowed[Student] {
    override val people: List[Student] = List(new Junior, new MasterOfScience, new PhD, new Student)
  }

  val listScholar: List[Scholar] = sigScholar.add(new Professor)
  val listStudent: List[Student] = new ScholasticInterestGroup_Add_Allowed[Student] {
    override val people: List[Student] = List(new Junior, new MasterOfScience, new PhD, new Student)
  }.add(new Student)


  class RulesOfConduct[-T] {
    def checkCompliance(scholar: T): Boolean = true

    /*
    * Let us assume that the following method is allowed: def noncompliedScholar():T = ???
    * Suppose we implement it and then we use it this way. Rules of conduct for PhD student
    * are definitely covered by the general rules of conduct for scholars, so all noncompliance
    * rule check for scholars can be applied to PhD students.
    *   val phdConduct: RulesOfConduct[PhD] = new RulesOfConduct[Scholar] {
    *     override def noncompliantScholar(): Scholar = new Untenured }
    *     val phdStudent = phdConduct.noncompliedScholar()
    *     As you can see we expect to obtain a noncompliant PhD student and instead we get an object of Untenured professor.
    * */
  }

  def annualCompianceCheck(rules: RulesOfConduct[Senior], scholars: List[Senior]): List[Scholar] = {
    scholars.filter(s => rules.checkCompliance(s))
  }

  annualCompianceCheck(new RulesOfConduct[Student], List(new Senior))

  /*If Professor is a subtype of Scholar, will ScholasticInterestGroup[Professor] <: ScholasticInterestGroup[Scholar]?
* type mismatch;
* found   : Variance.ScholarsVariances.ScholasticInterestGroup[Variance.HierarchyOfScholars.Professor]
* required: Variance.ScholarsVariances.ScholasticInterestGroup[Variance.HierarchyOfScholars.Scholar]
* Note: Variance.HierarchyOfScholars.Professor <: Variance.HierarchyOfScholars.Scholar, but class ScholasticInterestGroup is invariant in type T.
* You may wish to define T as +T instead. (SLS 4.5)
* val sigProfessor: ScholasticInterestGroup[Scholar] = new ScholasticInterestGroup[Professor]
* */
  /*
  * With covariance, +T we want to say that any SIG for professors is also a SIG for scholars.
  * In this case we can substitute a container of Professors in place where a container of Scholars is expected.
  * val sigProfessor: ScholasticInterestGroup[Scholar] = new ScholasticInterestGroup[Professor]
  * However, this expression is not allowed since the container is invariant
  * */
  /*
  * With contravariance, -T we say that rules of conduct for scholars also apply to professors,
  * however, not all rules for professors are applied to all scholars in general
  * */
  val rulesForProfessor: RulesOfConduct[Professor] = new RulesOfConduct[Scholar]

  class ScholasticInterestGroup_v1[+T] {
    val people: List[T] = List()
  }

  val sigProfessor_v1: ScholasticInterestGroup_v1[Scholar] = new ScholasticInterestGroup_v1[Professor]
  //  If the method add is permitted, we could re-assign the object of SIG professors to the var of SIG scholars
  //  and then add an object of some subtype of Scholars that is not Professors. Doing so will result in a list whose
  //  members are not professors any more. It is a mess!
  //  sigProfessor_v1.add(new Junior)
  //  case class RulesOfConduct_v1[-T](val readAndAcknowledged: T)

}
