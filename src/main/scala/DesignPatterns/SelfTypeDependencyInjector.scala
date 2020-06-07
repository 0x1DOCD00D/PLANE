/*
 * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package DesignPatterns

//We use a different approach to inject the functionality of storing data on devices
//into the object of the (sub)type UniversityPersonnel

object SelfType {

  //We mix this trait with the (sub)type UniversityPersonnel and we want to
  //access the methods of the type UniversityPersonnel
  trait StorageDevice {
    this: UniversityPersonnel =>
    //this method uses the methods of the class UniversityPersonnel
    //even though the method data2Store is abstract and the method
    //savePersonnelType is redefined in the derived classes
    def store:String = {
      savePersonnelType
      println(s"$data2Store")
      data2Store
    }
  }

  abstract class UniversityPersonnel {
    protected val UID: BigInt
    protected def data2Store:String

    //When this method is called on the object of the type Professor
    //it prints out DesignPatterns.SelfType$Professor
    protected [this] def savePersonnelType: Unit = println(this.getClass.getName)
  }

  class Student(override val UID: BigInt) extends UniversityPersonnel with StorageDevice{
    override protected def data2Store: String = UID.toByteArray.toList.toString
    override def savePersonnelType: Unit = println("We love CS students!")
  }

  class Professor(override val UID: BigInt) extends UniversityPersonnel with StorageDevice {
    override protected def data2Store: String = "Professor UIC: " + UID.toByteArray.toList.toString
  }

}

object SelfTypeDependencyInjector extends App {
  new SelfType.Student(12345).store
  new SelfType.Professor(67890).store
}
