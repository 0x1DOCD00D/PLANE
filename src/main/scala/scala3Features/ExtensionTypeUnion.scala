////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scala3Features

//https://mail.google.com/mail/u/0/#inbox/FMfcgzQbgcSnQShpngKnnZKWPLJnGVzj

object ExtensionTypeUnion {
  extension [T](t: T) def m(v: T): T = v
  val a: Int | String = 5.m("str")
  println(a.getClass)

  extension [T](t: T)
    inline def im[M >: T](m: M): T = inline m match
      case tm: T => tm

  extension [T](t: T)
    def mx[M](m: M)(using ev: M =:= T): T = ev(m)

//  val b: Int |String = 5.mx("str")
  val b: Int | String = 5.mx(7)
  //  3.im("str")
  def main(args: Array[String]): Unit =
    println(s"a = $a")
}
