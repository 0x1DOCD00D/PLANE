////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package DesignPatterns

//the pattern Adapter is used to change the interface of some Adaptee
object Adapter extends App {

  //the client makes calls to methods NewMethod1 and NewMethod2 that take no parameters
  //however, the class Adaptee does not provide these methods and other clients keep
  //invoking the method SomeCoreMethod
  class Adaptee {
    def SomeCoreMethod(msg: String): Unit = println(msg)
  }

  //this is one way to present an adapter as a trait that obtains access
  //to the method SomeCoreMethod
  trait Adapter {
    this: Adaptee =>
    def NewMethod1(): Unit = SomeCoreMethod(this.getClass.getName)

    def NewMethod2(): Unit = SomeCoreMethod(this.getClass.getPackageName)
  }

  //then we mix the trait with the adaptee and voila! - everything is ready
  val objectWithAdaptee = new Adaptee with Adapter
  objectWithAdaptee.NewMethod1()
  //but the original method is still available
  objectWithAdaptee.SomeCoreMethod("bummer!")

  //the other way to use an adapter is to use it as a new interface
  trait AdapterAbstract {
    def NewMethod1(): Unit

    def NewMethod2(): Unit
  }

  //whereby a class implements this new interface and uses the class Adaptee internally
  //to enable the functionality of the new interface
  class Adapted extends AdapterAbstract {
    private val adapteeObject = new Adaptee

    override def NewMethod1(): Unit = adapteeObject.SomeCoreMethod(this.getClass.getName)

    override def NewMethod2(): Unit = adapteeObject.SomeCoreMethod(this.getClass.getPackageName)
  }

  (new Adapted).NewMethod1()

  //finally, the adapter conversion can be done implicitly
  implicit class AutomaticAdapter(adaptee: Adaptee) {
    def NewMethod1(): Unit = adaptee.SomeCoreMethod(this.getClass.getName)

    def NewMethod2(): Unit = adaptee.SomeCoreMethod(this.getClass.getPackageName)
  }

  extension (adaptee: Adaptee)
    def anotherMethod = adaptee.SomeCoreMethod(this.getClass.getName)

  //AutomaticAdapter((new Adaptee)).NewMethod
  (new Adaptee).NewMethod1()
  (new Adaptee).NewMethod2()
  (new Adaptee).anotherMethod
}
