////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package TypeFP

object UniverseExample:

  given strFoundInt: "Found Int" = "Found Int"
  given strFoundString: "Found String" = "Found String"
  given errrr: "Error" = "Error"
  given anyFound: Any = 1
  // Define a match type that attempts to classify `T`.
  type Universe[T] = T match
    case Int    => "Found Int"
    case String => "Found String"
    case Any    => Universe[Any] // Self-reference for all cases
    case _      => Universe[T] // Fallback to recursive evaluation

  // Now, attempt to evaluate Universe[Any] at the type level
//  summon[Universe[Any]]  // This causes **compiler recursion limit error**
//  summon[Universe[Double]]
  summon[Universe[Int]]
  summon[Universe[String]]
  summon[Universe["Error"]]
  summon[Universe[1]]
