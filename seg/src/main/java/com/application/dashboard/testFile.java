package com.application.dashboard;

import java.io.*;
import com.application.dashboard.ReadFile;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import static javafx.application.Application.launch;

/**
 * This is a test file. It should be ignored.
 * */
public class testFile extends Application {
    private static final double MIN_CONTENT_WIDTH = 600;
    private static final double MIN_CONTENT_HEIGHT = 400;

    @Override
    public void start(Stage stage) throws Exception {
        StackPane root = new StackPane();
        root.layoutBoundsProperty().addListener((observable, oldLayoutBounds, newLayoutBounds) ->
                System.out.println(
                        "New scene size: " + newLayoutBounds.getWidth() + " x " + newLayoutBounds.getHeight()
                )
        );

        root.setMinSize(MIN_CONTENT_WIDTH, MIN_CONTENT_HEIGHT);
        root.setPrefSize(800, 600);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        System.out.println("Initial stage size: " + stage.getWidth() + " x " + stage.getHeight());
        System.out.println("Initial scene size: " + scene.getWidth() + " x " + scene.getHeight());

        double FRAME_WIDTH = stage.getWidth() - scene.getWidth();
        double FRAME_HEIGHT = stage.getHeight() - scene.getHeight();

        stage.setMinWidth(MIN_CONTENT_WIDTH + FRAME_WIDTH);
        stage.setMinHeight(MIN_CONTENT_HEIGHT + FRAME_HEIGHT);

        System.out.println("Min stage size: " + stage.getMinWidth() + " x " + stage.getMinHeight());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
