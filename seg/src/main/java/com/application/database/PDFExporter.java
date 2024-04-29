package com.application.database;




import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.Chart;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class PDFExporter {

    public void exportChartToPDF(Chart chart) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (PDDocument document = new PDDocument()) {
                // Create a PDF page for the chart
                PDPage page = new PDPage();
                float width = (float) chart.getWidth();
                float height = (float) chart.getHeight();

                // Set the page size based on the dimensions of the chart
                page.setMediaBox(new PDRectangle(width, height));
                document.addPage(page);

                // Take a snapshot of the chart
                BufferedImage bufferedImage = chartToImage(chart);

                // Embed the chart image in the PDF
                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    PDImageXObject imageXObject = LosslessFactory.createFromImage(document, bufferedImage);
                    contentStream.drawImage(imageXObject, 0, 0, width, height); // Adjust position and size as needed
                }

                // Save the PDF document
                document.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private BufferedImage chartToImage(Chart chart) {
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setDepthBuffer(true);
        WritableImage image = chart.snapshot(parameters, null);
        return SwingFXUtils.fromFXImage(image, null);
    }
}
}




