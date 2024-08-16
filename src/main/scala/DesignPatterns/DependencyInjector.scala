/*
 * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package DesignPatterns

//this trait describes the abstract behavior of an abstract storage device that stores a list of bytes
//to implement concrete storage, it should extend this trait to enforce the contract on the behavior
trait StorageDevice {
  def store(data: List[Byte]): Unit
}

//to implement a concrete storage device, we extend the trait and create
//the corresponding classes with the implementations of the abstract method store
class MagneticTape extends StorageDevice {
  override def store(data: List[Byte]): Unit = println(s"MT: ${data.toString()}")
}

class HDD extends StorageDevice {
  override def store(data: List[Byte]): Unit = println(s"DesignPatterns.HDD: ${data.toString()}")
}


//this is a general type for the university personnel. Every person has the university ID.
//the behavior is to store the personnel info into some database on some storage
//To do that, we inject the object of the type StorageDevice into the objects whose types
// are the subtype of the type UniversityPersonnel. Using the subtype polymorphism
//we can vary different types of storage objects to store personnel data
abstract class UniversityPersonnel(val storeDev:StorageDevice) {
  val UID: BigInt
  def store:Unit
}

class Student(id:BigInt, st:StorageDevice) extends UniversityPersonnel(st){
  override val UID: BigInt = id

  //the method is polymorphic and its behavior depends on the storage type
  //object that is injected in this class
  def store:Unit = st.store(UID.toByteArray.toList)
}

class Professor(id:BigInt, st:StorageDevice) extends UniversityPersonnel(st){
  override val UID: BigInt = id

  def store:Unit = st.store(UID.toByteArray.toList)
}


object DependencyInjector extends App {
  new Student(12345,new MagneticTape).store
  new Professor(7890, new HDD).store
}
