package com.application.files;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.DirectoryChooser;
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
    private static FilePathHandler pathHandler;
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

    public static void setfph(FilePathHandler fph){
        pathHandler = fph;
    }
    public void openFileBox(String filename) throws RuntimeException {
        // Open file chooser dialog
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select "+ filename + " file/files");
        fileChooser.setInitialDirectory(pathHandler.getFilesPath());
        ExtensionFilter ex1 = new ExtensionFilter("CSV Files", "*.csv");
        fileChooser.getExtensionFilters().add(ex1);
        List<File> selectedFile = fileChooser.showOpenMultipleDialog(null);


        try {
            pathHandler.fileTypeHandler(selectedFile);
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING ,"error with the file handling system" , ButtonType.OK);
            alert.showAndWait();
        throw new RuntimeException(e);}
    }

    public File openSingleFileBox(String filename) {
        // Open file chooser dialog
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select "+ filename + " file");
        fileChooser.setInitialDirectory(pathHandler.getFilesPath());
        ExtensionFilter ex1 = new ExtensionFilter("CSV Files", "*.csv");

        fileChooser.getExtensionFilters().add(ex1);

        return fileChooser.showOpenDialog(null);
    }

    public void openDirectory(String directory) {
        // Open file chooser dialog
        DirectoryChooser fileChooser = new DirectoryChooser();
        fileChooser.setTitle("Select "+ directory + " file/es");
        fileChooser.setInitialDirectory(pathHandler.getFilesPath());
        File selectedFile = fileChooser.showDialog(null);
        try {
            pathHandler.directoy_handler(selectedFile);
        }catch (Exception e){

        }

    }

    public String selectFolderPath(){
        String path = "";
        DirectoryChooser directoryChooser = new DirectoryChooser();

        File localPath = directoryChooser.showDialog(null);

        pathHandler.setFilesPath(localPath);

        return localPath.toString();
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