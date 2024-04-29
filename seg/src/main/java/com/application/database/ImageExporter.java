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

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImageExporter {
    public void exportChartToImage(Chart chart){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
        File file = fileChooser.showSaveDialog(null);
        try{
            ImageIO.write(chartToImage(chart), "png", file);
                System.out.println();
        }catch(IOException e){

        }
    }


    private BufferedImage chartToImage(Chart chart) {
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setDepthBuffer(true);
        WritableImage image = chart.snapshot(parameters, null);
        return SwingFXUtils.fromFXImage(image, null);
    }
}
