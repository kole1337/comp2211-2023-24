package com.application.files;

import com.opencsv.CSVReader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FilePathHandler {
    private File clickPath;
    private File impressionPath;
    private File serverPath;
    private static ArrayList<String> notFound;
    private static File temp;
    private static String firstl;

    public File getFilesPath() {
        return filesPath;
    }

    public void setFilesPath(File filesPath) {
        this.filesPath = filesPath;
    }

    private File filesPath;
    private Logger logger = Logger.getLogger(getClass().getName());


    public boolean fileTypeHandler(List<File> files){
        if(!clickPath.exists()){
            notFound.add("clicklog");
        }
        if(!impressionPath.exists()){
            notFound.add(2);
        }
        if(!serverPath.exists()){
            notFound.add(3);
        }
        try{

            for (int i = 0; i < files.size(); i++) {
                FileReader filereader = new FileReader(files.get(i));
                CSVReader csvReader = new CSVReader(filereader);
                System.out.println("reading");
                String[] nextRecord;

                nextRecord = csvReader.readNext();
                String firstLn = "";
                for (int j = 0; j < nextRecord.length; j++) {
                    firstLn += nextRecord[j];
                }
                firstLn = firstLn.toLowerCase();
                firstLn = firstLn.replaceAll("\\s+", "");
                System.out.println(firstLn);
                temp = files.get(i);
                if (firstLn.equals("dateidclickcost") && clickPath.exists()) {
                    Alert a1 = new Alert(Alert.AlertType.WARNING , "Duplicate clicklog selected file  found! \n Replace " + clickPath.getName() + " with " + temp.getName() + " ?", ButtonType.OK, ButtonType.CANCEL);
                    replace_click(a1);
                }else if(firstLn.equals("dateidclickcost")){
                    setClickPath(files.get(i).getName());
                    notFound.remove("clicklog");
                }
                if (firstLn.equals("dateidgenderageincomecontextimpressioncost")&& impressionPath.exists()) {
                    Alert a1 = new Alert(Alert.AlertType.WARNING , "Duplicate impressionlog selected file  found! \n Replace " + clickPath.getName() + " with " + temp.getName() + " ?", ButtonType.OK, ButtonType.CANCEL);
                    replace_imp(a1);
                }else if(firstLn.equals("dateidgenderageincomecontextimpressioncost")){
                    setImpressionPath(files.get(i).getName());
                    notFound.remove("impressionlog");
                }
                if (firstLn.equals("entrydateidexitdatepagesviewedconversion")&& serverPath.exists()) {
                    Alert a1 = new Alert(Alert.AlertType.WARNING , "Duplicate serverlog selected file found! \n Replace " + clickPath.getName() + " with " + temp.getName() + " ?", ButtonType.OK, ButtonType.CANCEL);
                    replace_server(a1);
                }else if(firstLn.equals("entrydateidexitdatepagesviewedconversion")){
                    setServerPath(files.get(i).getName());
                    notFound.remove(3);
                }
            }
        }catch (Exception e){
            return false;
        }
        if(!notFound.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING ,"files: " + notFound + "were not found" , ButtonType.OK);
            alert.showAndWait();
        }
        return notFound.isEmpty();
    }
    public boolean directoy_handler(File dir)throws Exception{
        if(dir.list() == null){
            throw new FileNotFoundException("directory is empty");
        }
        File[] files = dir.listFiles();
        try {
            if(!clickPath.exists()){
                notFound.add("clicklog");
            }
            if(!impressionPath.exists()){
                notFound.add("impressionlog");
            }
            if(!serverPath.exists()){
                notFound.add("serverlog");
            }
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().endsWith(".csv")) {
                    FileReader filereader = new FileReader(files[i]);
                    CSVReader csvReader = new CSVReader(filereader);
                    String[] nextRecord;
                    nextRecord = csvReader.readNext();
                    Alert alert;
                    String firstLn = "";
                    for (int j = 0; j < nextRecord.length; j++) {
                        firstLn += nextRecord[j];
                    }
                    firstLn = firstLn.toLowerCase();
                    firstLn = firstLn.replaceAll("\\s+", "");
                    firstl = firstLn;
                    if(firstLn.equals("dateidclickcost") & clickPath.exists() && notFound.contains("clicklog")){
                        Alert a1 = new Alert(Alert.AlertType.WARNING , "Duplicate clicklog file in directory " + dir.getName() + " found! \n replace " + clickPath.getName() + " with " + temp.getName() + " ?", ButtonType.OK, ButtonType.CANCEL);
                        replace_click(a1);
                    }   else if (firstLn.equals("dateidclickcost") && notFound.contains("clicklog")) {
                        setClickPath(files[i].getAbsolutePath());
                        logger.log(Level.INFO, "filepath " + files[i].getAbsolutePath() + "saved");
                    } else if (firstLn.equals("dateidclickcost")) {
                        Alert a1 = new Alert(Alert.AlertType.WARNING , "Another clicklog was found in " + dir.getName() + " \n do you want to replace " + clickPath.getName() + " with " + temp.getName() + " ?", ButtonType.OK, ButtonType.CANCEL);
                        replace_click(a1);
                    }
                    if(firstLn.equals("dateidgenderageincomecontextimpressioncost") & clickPath.exists() && notFound.contains(2)){
                        Alert a1 = new Alert(Alert.AlertType.WARNING , "Duplicate impressionlog file in directory: " + dir.getName() + " found! \n replace " + clickPath.getName() + " with " + temp.getName() + " ?", ButtonType.OK, ButtonType.CANCEL);
                        replace_imp(a1);
                    }   else if (firstLn.equals("dateidgenderageincomecontextimpressioncost") && notFound.contains("impressionlog")) {
                        setImpressionPath(files[i].getAbsolutePath());
                        logger.log(Level.INFO, "filepath " + files[i].getAbsolutePath() + "saved");
                    } else if (firstLn.equals("dateidgenderageincomecontextimpressioncost")) {
                        Alert a1 = new Alert(Alert.AlertType.WARNING , "Another impressionlog was found in " + dir.getName() + " \n do you want to replace " + clickPath.getName() + " with: " + temp.getName() + " ?", ButtonType.OK, ButtonType.CANCEL);
                        replace_imp(a1);
                    }
                    if(firstLn.equals("entrydateidexitdatepagesviewedconversion") & clickPath.exists() && notFound.contains("serverlog")){
                        Alert a1 = new Alert(Alert.AlertType.WARNING , "Duplicate serverlog file in directory: " + dir.getName() + " found! \n replace " + clickPath.getName() + " with " + temp.getName() + " ?", ButtonType.OK, ButtonType.CANCEL);
                        replace_server(a1);
                    }   else if (firstLn.equals("entrydateidexitdatepagesviewedconversion") && notFound.contains("serverlog")) {
                        setServerPath(files[i].getAbsolutePath());
                        logger.log(Level.INFO, "filepath " + files[i].getAbsolutePath() + "saved");
                    } else if (firstLn.equals("entrydateidexitdatepagesviewedconversion")) {
                        Alert a1 = new Alert(Alert.AlertType.WARNING , "Another serverlog was found in " + dir.getName() + " \n do you want to replace " + clickPath.getName() + " with: " + temp.getName() + " ?", ButtonType.OK, ButtonType.CANCEL);
                        replace_server(a1);
                    }
                }
            }
        }catch (Exception e){throw new RuntimeException("error with the file system");}
        if(notFound.contains("clicklog")&& clickPath.exists()){
            notFound.remove("clicklog");
        }
        if(notFound.contains("impressionlog")&&impressionPath.exists()){
            notFound.remove("impressionlog");
        }
        if(notFound.contains("serverlog")&&impressionPath.exists()){
            notFound.remove(3);
        }
        if(!notFound.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING ,"files: " + notFound + "were not found" , ButtonType.OK);
            alert.showAndWait();
        }
        return notFound.isEmpty();
    }

    private void replace_click(Alert a1) {
        a1.setTitle("duplicate clicklog");
        ((Button) a1.getDialogPane().lookupButton(ButtonType.OK)).setText("replace");
        ((Button) a1.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("keep");
        Optional<ButtonType> result = a1.showAndWait();
        if (result.get() == ButtonType.OK){
            logger.log(Level.INFO, "replacing clickpath");
            setClickPath(temp.getAbsolutePath());
        }
    }

    private void replace_server(Alert a1) {
        a1.setTitle("duplicate serverlog");
        ((Button) a1.getDialogPane().lookupButton(ButtonType.OK)).setText("replace");
        ((Button) a1.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("keep");
        Optional<ButtonType> result = a1.showAndWait();
        if (result.get() == ButtonType.OK){
            logger.log(Level.INFO, "replacing serverlog");
            setServerPath(temp.getAbsolutePath());
        }
    }

    private void replace_imp(Alert a1) {
        a1.setTitle("duplicate impressionlog");
        ((Button) a1.getDialogPane().lookupButton(ButtonType.OK)).setText("replace");
        ((Button) a1.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("keep");
        Optional<ButtonType> result = a1.showAndWait();
        if (result.get() == ButtonType.OK){
            logger.log(Level.INFO, "replacing impressionlog");
            setImpressionPath(temp.getAbsolutePath());
        }
    }


    public void setClickPath(String csvPath) {
        logger.log(Level.INFO, "Setting click_log path.");
        this.clickPath = new File(csvPath);
    }

    public void setImpressionPath(String impressionPath) {
        logger.log(Level.INFO, "Setting impression_log path.");
        this.impressionPath = new File(impressionPath);
    }

    public void setServerPath(String serverPath) {
        logger.log(Level.INFO, "Setting server_log path.");
        this.serverPath = new File(serverPath);
    }

    public File getClickPath() {
        return clickPath;
    }

    public File getImpressionPath() {
        return impressionPath;
    }

    public File getServerPath() {
        return serverPath;
    }
}

