package com.application.files;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FileChooser {

    private FilePathHandler pathHandler = new FilePathHandler();
    public static String main() {
        // Open file chooser dialog
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("sex");
        int returnValue = fileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());

            // Open the selected file in the file explorer
            openFileInExplorer(selectedFile);
            return (selectedFile.getAbsolutePath());
        } else {
            System.out.println("No file selected.");
        }
        return null;
    }

    private static void openFileInExplorer(File file) {
        // Check if Desktop is supported
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();

            // Check if opening a file is supported
            if (desktop.isSupported(Desktop.Action.OPEN)) {
                try {
                    // Open the file in the default file explorer
                    desktop.open(file.getParentFile()); // Open parent directory
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Opening a file is not supported on this platform.");
            }
        } else {
            System.out.println("Desktop is not supported on this platform.");
        }
    }
}

