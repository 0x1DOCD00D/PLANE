
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scala3Features

import scala3Features.Opaqueness.ReadOrWrite

//https://docs.scala-lang.org/scala3/reference/other-new-features/opaques.html

object Opaqueness:
  opaque type Permissions = Int
  opaque type PermissionChoice = Int
  opaque type Permission <: Permissions & PermissionChoice = Int

  extension (x: PermissionChoice)
    def |(y: PermissionChoice): PermissionChoice = x | y
  extension (x: Permissions)
    def &(y: Permissions): Permissions = x | y
  extension (granted: Permissions)
    def is(required: Permissions): Boolean = (granted & required) == required
    def isOneOf(required: PermissionChoice): Boolean = (granted & required) != 0

  val NoPermission: Permission = 0
  val Read: Permission = 1
  val Write: Permission = 2
  val ReadWrite: Permissions = Read | Write
  val ReadOrWrite: PermissionChoice = Read | Write
  val i:Int = NoPermission

object User:
  import Opaqueness.*

  case class Item(rights: Permissions)

  extension (item: Item)
    def +(other: Item): Item = Item(item.rights & other.rights)

  val roItem: Item = Item(Read) // OK, since Permission <: Permissions
  val woItem: Item = Item(Write)
  val rwItem: Item = Item(ReadWrite)
  val noItem: Item = Item(NoPermission)

  assert(!roItem.rights.is(ReadWrite))
  assert(roItem.rights.isOneOf(ReadOrWrite))

  assert(rwItem.rights.is(ReadWrite))
  assert(rwItem.rights.isOneOf(ReadOrWrite))

  assert(!noItem.rights.is(ReadWrite))
  assert(!noItem.rights.isOneOf(ReadOrWrite))

  assert((roItem + woItem).rights.is(ReadWrite))
end User

@main def runOpaqueness(args: String*): Unit =
  println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/scala3Features/Opaqueness.scala created at time 2:03PM")
  
  println(ReadOrWrite)

