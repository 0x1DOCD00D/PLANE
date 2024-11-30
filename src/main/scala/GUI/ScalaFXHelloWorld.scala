
////////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
// Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
////////////////////////////////////////////////////////////////////////////////

package GUI

import scalafx.application.JFXApp3

import scalafx.application.JFXApp3
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.effect.DropShadow
import scalafx.scene.layout.HBox
import scalafx.scene.paint.*
import scalafx.scene.paint.Color.*
import scalafx.scene.text.Text

import scala.language.implicitConversions

object ScalaFXHelloWorld extends JFXApp3:

  override def start(): Unit =

    stage = new JFXApp3.PrimaryStage:
      //    initStyle(StageStyle.Unified)
      title = "ScalaFX Hello World"
      scene = new Scene:
        fill = Color.rgb(38, 38, 38)
        content = new HBox:
          padding = Insets(50, 80, 50, 80)
          children = Seq(
            new Text:
              text = "Scala"
              style = "-fx-font: normal bold 100pt sans-serif"
              fill = new LinearGradient(endX = 0, stops = Stops(Red, DarkRed))
            ,
            new Text:
              text = "FX"
              style = "-fx-font: italic bold 100pt sans-serif"
              fill = new LinearGradient(endX = 0, stops = Stops(White, DarkGray))
              effect = new DropShadow:
                color = DarkGray
                radius = 15
                spread = 0.25
          )