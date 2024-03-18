package com.application.files;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.*;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

//import javafx.stage.FileChooser;
public class FileChooserWindow {
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

    public List<File> openFileBox() {
        // Open file chooser dialog
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open 1st file");
        ExtensionFilter ex1 = new ExtensionFilter("CSV Files", "*.csv");

        fileChooser.getExtensionFilters().add(ex1);
        List<File> selectedFile = fileChooser.showOpenMultipleDialog(null);

        if(selectedFile != null){
            //System.out.println("Open file: " + selectedFile.getAbsolutePath());
        }
        FilePathHandler obj = new FilePathHandler();
        obj.fileTypeHandler(selectedFile);
        System.out.println(obj.getClickPath());
        return selectedFile;


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