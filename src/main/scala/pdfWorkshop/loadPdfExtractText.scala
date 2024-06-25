/*
 * Copyright (c) 2024 Mark Grechanik and Lone Star Consulting, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the specific language governing permissions and limitations under the License.
 */

package pdfWorkshop

import org.apache.pdfbox.pdmodel.{PDDocument, PDPageTree}
import org.apache.pdfbox.Loader
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.interactive.action.{PDActionGoTo, PDActionURI}
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationLink
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.{
  PDDestination,
  PDNamedDestination,
  PDPageDestination
}
import org.apache.pdfbox.text.{PDFTextStripper, PDFTextStripperByArea}

import java.awt.geom.Rectangle2D
import java.io.File
import scala.util.{Failure, Success, Try}

object loadPdfExtractText:
  @main def runloadPdfExtractText(args: String*): Unit =
    println(
       "File /Users/drmark/IdeaProjects/PLANE/src/main/scala/pdfWorkshop/loadPdfExtractText.scala created at time 10:47 AM"
    )
    val jls22 = this.getClass().getResource("/JLS22.pdf")
    val stripperByArea = new PDFTextStripperByArea()

    val result: Either[String, String] = Try(Loader.loadPDF(new File(jls22.toURI()))) match
      case Success(doc) =>
        val pages: PDPageTree = doc.getPages
        pages
          .iterator()
          .forEachRemaining(page =>
            page.getAnnotations.forEach(annot =>
              if annot.isInstanceOf[PDAnnotationLink] then
                val link = annot.asInstanceOf[PDAnnotationLink]
                val action: Any = annot.asInstanceOf[PDAnnotationLink].getAction
                if action.isInstanceOf[PDActionGoTo] then
                  val destination: PDDestination = action.asInstanceOf[PDActionGoTo].getDestination
                  if destination.isInstanceOf[PDNamedDestination] then
                    val nd = destination.asInstanceOf[PDNamedDestination].getNamedDestination
                    println(nd)
                  else if destination.isInstanceOf[PDPageDestination] then
                    val pd = destination.asInstanceOf[PDPageDestination]
                    val pn = pd.retrievePageNumber

                    val rect: PDRectangle = link.getRectangle();
                    val x = rect.getLowerLeftX();
                    var y = rect.getUpperRightY();
                    val width = rect.getWidth();
                    val height = rect.getHeight();
                    val rotation = page.getRotation();
                    if (rotation == 0) {
                      val pageSize = page.getMediaBox();
                      y = pageSize.getHeight() - y;
                    }
                    val rect2D: Rectangle2D.Float = new Rectangle2D.Float(x, y, width, height)
                    stripperByArea.addRegion("gmark", rect2D);
                    stripperByArea.extractRegions(page)
                    val txt = stripperByArea.getTextForRegion("gmark")
                    println(
                       s"Page number: $pn with link ${txt}"
                    )
            )
          )
        val stripper = PDFTextStripper()
        Right(stripper.getText(doc))
      case Failure(exception) =>
        Left(s"Failed to load PDF: $exception")
    println(result.isRight)
