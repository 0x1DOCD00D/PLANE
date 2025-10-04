
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package CatsIO.Concepts

import cats.effect.{ExitCode, IO, IOApp}

object IOAppRunWithCommandLine extends IOApp:
  override def run(args: List[String]): IO[ExitCode] =
    if (args.headOption.contains("--filename"))
      IO.println("Command line option --filename specified").as(ExitCode.Success)
    else
      IO.println(args.mkString).as(ExitCode(-1))