////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package scala3Features

//https://users.scala-lang.org/t/solved-scala-3-enum-case-with-body/10802/4

trait Error:
  def message: String

sealed trait AssetError extends Error

enum NamespaceError extends AssetError:
  case BlankInput

  case InvalidCharacter(input: String, char: Char)

  case Other(override val message: String)

  override def message: String = "hello"
