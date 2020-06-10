/*
 *
 *  * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package InterestingScalaFeatures

//our goal is to show how different Scala constructs allow programmers
//to create a way to compare parameterized containers in a way that
//disallows comparison of containers that hold objects of different types.
//That is, we want the compiler to prohibit the comparison of container
//of apples with a container of oranges
object OrderedContainer {

  //the basic container type is parameterized and it extends the type Ordered that provides the comparion interface
  trait Container[A] extends Ordered[A]

  //We creat our container, MyContainer that extends the parameterization of the parent Container
  //by the same type, MyContainer, so that we can compare MyContainer objects
  class MyContainer extends Container[MyContainer] {
    override def compare(that: MyContainer): Int = 0
  }

  //Here is a different container that we create for Students
  class StudentContainer extends Container[StudentContainer] {
    override def compare(that: StudentContainer) = 1
  }

  //the compiler allows it. Ouch! It ain't right
  //programmers should be able to use only the derived type for the parameterization
  class MarkContainer extends Container[StudentContainer] {
    override def compare(that: StudentContainer) = 1
  }

  //the type parameter, T is the subtype of the bounded container parameterized by T
  trait BndContainer[T <: BndContainer[T]] extends Ordered[T]

  class MyBContainer extends BndContainer[MyBContainer] {
    override def compare(that: MyBContainer): Int = 1
  }

  //Error: type arguments [Int] do not conform to trait BndContainer's type parameter bounds [T <: InterestingScalaFeatures.OrderedContainer.BndContainer[T]]
  //class MyBContainerWrong extends BndContainer[Int] {
  //  class MyBContainerWrong extends BndContainer[Int] {
  //    override def compare(that: Int): Int = 1
  //  }

  //but this is still permitted and it is wrong!
  //somehow we need to make sure that the compiler checks the type of the derived class
  //the container SomeKindOfContainer can be compared with MyBContainer and it ain't right
  class SomeKindOfContainer extends BndContainer[MyBContainer] {
    override def compare(that: MyBContainer): Int = 1
  }

}

object OrderedCorrectlyContainer {

  trait MyOrdered {
    // type This <: MyOrdered          // This will let SomeKindOfContainer compile
    type This >: this.type // The lower bound is set to implementing class's type
    def compare(that: This): Int
  }

  trait Container extends MyOrdered

  class MyContainer extends Container {
    type This = MyContainer

    override def compare(that: MyContainer): Int = 1
  }

  class SomeKindOfContainer extends Container {
    type This = SomeKindOfContainer

    //type This = MyContainer
    // Error: incompatible type in overriding
    // type This >: SomeKindOfContainer.this.type (defined in trait MyOrdered);
    //  found   : MyContainer
    //  required:  >: ShadyContainer.this.type
    //override def compare(that: MyContainer): Int = 1
    override def compare(that: SomeKindOfContainer): Int = 1
  }

  object CleanerOrdered {

    trait Container[T] extends Ordered[T] {
      this: T => // Restrict self-type to T, essentially restricting T to the implementing class
    }

    class MyContainer extends Container[MyContainer] {
      override def compare(that: MyContainer): Int = 1
    }

    //    class OtherContainer extends Container[MyContainer] {
    //      // Error: illegal inheritance;
    //      // self-type OtherContainer does not conform to Container[MyContainer]'s selftype Container[MyContainer] with MyContainer
    //      override def compare(that: OtherContainer): Int = 1
    //    }
  }

}
