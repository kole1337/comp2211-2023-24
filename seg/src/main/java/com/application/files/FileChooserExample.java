package com.application.files;

import javafx.application.Application;
        import javafx.application.Platform;
        import javafx.stage.FileChooser;
        import javafx.stage.Stage;

        import java.io.File;

        import javafx.application.Application;
        import javafx.stage.FileChooser;
        import javafx.stage.Stage;

        import java.io.File;

public class FileChooserExample extends Application {
    private static File selectedFile;

    public static void main(String[] args) {
        launch(args); // Launches the JavaFX application
    }

    @Override
    public void start(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a File");

        // Show open dialog
        selectedFile = fileChooser.showOpenDialog(primaryStage);

        // Get the path of the selected file
        if (selectedFile != null) {
            String selectedFilePath = selectedFile.getAbsolutePath();
            System.out.println("Selected file: " + selectedFilePath);
        } else {
            System.out.println("No file selected.");
        }

        // Close the JavaFX application
        Platform.exit();
    }

    public static String getSelectedFile() {
        return selectedFile.getAbsolutePath();
    }
}



