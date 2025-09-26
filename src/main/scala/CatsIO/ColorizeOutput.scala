////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO

object ColorizeOutput:
  def wrap(s: String, code: String) = s"$code$s${Console.RESET}"

extension (s: String)
  def red: String = ColorizeOutput.wrap(s, Console.RED)
  def green: String = ColorizeOutput.wrap(s, Console.GREEN)
  def yellow: String = ColorizeOutput.wrap(s, Console.YELLOW)
  def blue: String   = ColorizeOutput.wrap(s, Console.BLUE)
  def bold: String   = ColorizeOutput.wrap(s, Console.BOLD)
  def u: String      = ColorizeOutput.wrap(s, Console.UNDERLINED)

@main def pretty(): Unit =
  println("Success".green)
  println("Heads up".yellow.bold)
  println("Docs".blue.u + " are over there.")

