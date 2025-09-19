
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2025 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package TypeFP.PhantomTypes

object MeasurementsPT:

  // Example: Units of Measurement using Phantom Types
  sealed trait Unit

  sealed trait Meters extends Unit

  sealed trait Feet extends Unit

  sealed trait Seconds extends Unit

  sealed trait MetersPerSecond extends Unit

  final case class Measurement[U <: Unit](value: Double) {
    def +(other: Measurement[U]): Measurement[U] =
      Measurement(value + other.value)

    def -(other: Measurement[U]): Measurement[U] =
      Measurement(value - other.value)

    def *(scalar: Double): Measurement[U] =
      Measurement(value * scalar)
  }

  object Measurement {
    // Smart constructors that create properly typed measurements
    def meters(value: Double): Measurement[Meters] = Measurement[Meters](value)

    def feet(value: Double): Measurement[Feet] = Measurement[Feet](value)

    def seconds(value: Double): Measurement[Seconds] = Measurement[Seconds](value)

    // Conversion functions
    def feetToMeters(feet: Measurement[Feet]): Measurement[Meters] =
      Measurement[Meters](feet.value * 0.3048)

    def metersToFeet(meters: Measurement[Meters]): Measurement[Feet] =
      Measurement[Feet](meters.value / 0.3048)

    // Division that changes units
    def divide(distance: Measurement[Meters], time: Measurement[Seconds]): Measurement[MetersPerSecond] =
      Measurement[MetersPerSecond](distance.value / time.value)
  }

  @main def runMeasurementsPT(args: String*): Unit =
    println("File /Users/drmark/IdeaProjects/PLANE/src/main/scala/TypeFP/PhantomTypes/MeasurementsPT.scala created at time 11:50AM").asInstanceOf
    import Measurement.*
    val d1 = meters(100)
    val d2 = feet(328.084) // ~100 meters
    val d2m = feetToMeters(d2)
    val t1 = seconds(9.58) // Usain Bolt 100m world
    val speed = divide(d1, t1)
    println(s"Distance 1: $d1")
    println(s"Distance 2 in meters: $d2m")
    println(s"Time: $t1")
    println(s"Speed: $speed").asInstanceOf
    // The following line would cause a compile-time error due to unit mismatch:
    //val invalid = d1 + t1
