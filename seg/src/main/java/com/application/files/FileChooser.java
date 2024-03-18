package com.application.files;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class FileChooser {
    // Assuming FilePathHandler class definition is elsewhere
    private FilePathHandler pathHandler = new FilePathHandler();
    private static Integer numberOfFiles = 3;
    private static FileFilter filter = new FileFilter() {
        @Override
        public boolean accept(File file) {
            return file.getName().endsWith(".csv");
        }
        @Override
        public String getDescription() {
            return null;
        }
    };
    public static String[] main() { // Change return type to String[]
        // Open file chooser dialog
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(filter);
        fileChooser.setDialogTitle("Select files"); // Updated for clarity
        fileChooser.setMultiSelectionEnabled(true); // Enable multiple file selection

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = fileChooser.getSelectedFiles(); // Get all selected files
            String[] filePaths = new String[numberOfFiles];

            // Iterate over selected files and print paths
            for (int i = 0; i < 3; i++) {
                System.out.println("Selected file: " + selectedFiles[i].getAbsolutePath());
                filePaths[i] = selectedFiles[i].getAbsolutePath();
            }

            return filePaths; // Return the paths of selected files
        } else {
            System.out.println("No files selected.");
        }
        return null;
    }

    // The openFileInExplorer method remains unchanged
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