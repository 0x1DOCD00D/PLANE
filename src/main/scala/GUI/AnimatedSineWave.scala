////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package GUI

import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.chart.{LineChart, NumberAxis, XYChart}
import scalafx.animation.{KeyFrame, Timeline}
import scalafx.util.Duration
import scalafx.Includes.*

//https://users.scala-lang.org/t/best-way-to-program-a-gui/8540/15

object AnimatedSineWave extends JFXApp3 {
  private val MaxX = 360

  override def start(): Unit = {
    val xAxis = new NumberAxis(0, MaxX, 45):
      label = "X"
    val yAxis = new NumberAxis(-120, 120, 20):
      label = "Y"

    val lineChart = new LineChart[Number, Number](xAxis, yAxis):
      title = "Sine Wave Animation"
      legendVisible = false
      animated = false // do not show trails

    val series = new XYChart.Series[Number, Number]()
    lineChart.getData.add(series)

    // Timeline for animation
    val timeline = new Timeline:
      cycleCount = Timeline.Indefinite
      keyFrames = Seq(
         KeyFrame(
            Duration(10),
            onFinished = _ => {
              if series.data().size > MaxX then
                // Start from the beginning
                series.data().clear()
              else
                // Add the next point in the sine wave
                val x = series.data().size
                val y = 100 * Math.sin(2 * Math.PI * x / 360d)
                series.data() += XYChart.Data[Number, Number](x, y)
            }
         )
      )
    timeline.play()

    stage = new JFXApp3.PrimaryStage:
      title = "Animated Sine Wave"
      scene = new Scene(800, 600):
        root = lineChart
  }
}
