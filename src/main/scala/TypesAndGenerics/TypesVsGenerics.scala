/*
 * Copyright (c) 2020 Mark Grechanik. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package TypesAndGenerics

/*
 * In this example we consider how to specialize classes using types. We design a framework for storing objects.
 * In fact, we do not actually implement any code to store objects, and our goal is to figure out how to create
 * generalized types for the storage framework and then to specialize them for specific object types to store.
 */
abstract class Objects
abstract class StorageMedium
abstract class DataFormat

//in this trait we use type aliases whose values will be defined
//in the types that extend this trait
trait StoreData {
  //we declare type aliases that are bound by the corresponding abstract types
  //when instantiating an object of the type StoreData these type variables should
  //be assigned the values of some concrete types within the specified bounds
  type WhatObjects2Store <: Objects
  type Where2Store <: StorageMedium
  type WhatFormat <: DataFormat

  //this method takes the parameters of the types that are the subtypes of
  //the abstract types that define the upper bounds
  def StoreIt(objects: Objects, medium: StorageMedium, format: DataFormat) =
    println(s"Storing $objects on $medium in the format $format")

  //this method takes the parameters whose types are defined by the type variables/aliases
  def StoreItTyped(objects: WhatObjects2Store, medium: Where2Store, format: WhatFormat) =
    println(s"Storing $objects on $medium in the format $format")
}

//this trait is defined using generics, i.e., type variables with bounds.
//doing so is equivalent to using type aliases.
trait StoreData_Generic[WhatObjects2Store <: Objects, Where2Store <: StorageMedium, WhatFormat <: DataFormat] {
  //not much difference in the definition of this method from the one in the trait
  //where types aliases are used
  def StoreItTyped(objects: WhatObjects2Store, medium: StorageMedium, format: WhatFormat) =
    println(s"Storing $objects on $medium in the format $format")

}

//let's create some concrete types, shall we?
case class JavaObject() extends Objects
case class CppObject() extends Objects
case class MagneticTape() extends StorageMedium
case class JavaSerializationFormat() extends DataFormat

//of course, we must override type aliases with concrete reified type names
class JavaObjectDump extends StoreData{
  override type WhatObjects2Store = JavaObject
  override type Where2Store = MagneticTape
  override type WhatFormat = JavaSerializationFormat
}

//how do you like this style with generic compared to type aliases?
class JavaObjectDump_Generic extends StoreData_Generic[JavaObject, MagneticTape, JavaSerializationFormat]{
}

//and now we can instantiate these types and run the program
object TypesVsGenerics extends App {
  val jObjectStore = new JavaObjectDump
  val jObjectStore_Generic = new JavaObjectDump_Generic
  jObjectStore.StoreIt(JavaObject(), MagneticTape(), JavaSerializationFormat())
  jObjectStore.StoreItTyped(JavaObject(), MagneticTape(), JavaSerializationFormat())
  jObjectStore.StoreIt(CppObject(), MagneticTape(), JavaSerializationFormat())
  jObjectStore_Generic.StoreItTyped(JavaObject(), MagneticTape(), JavaSerializationFormat())
  //this expression is not allowed by the type checker since the type CppObject is not
  //a subtype of the type JavaObject.
//  jObjectStore.StoreItTyped(CppObject(), MagneticTape(), JavaSerializationFormat())
}
