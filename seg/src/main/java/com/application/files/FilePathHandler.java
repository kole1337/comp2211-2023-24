package com.application.files;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
    private static boolean action;

    public File getFilesPath() {
        return filesPath;
    }
    public void innit(){
        notFound = new ArrayList<>();
        notFound.add("clicklog");
        notFound.add("impressionlog");
        notFound.add("serverlog");
        action = false;

    }
    public void setFilesPath(File filesPath) {
        this.filesPath = filesPath;
    }

    private File filesPath;
    private Logger logger = Logger.getLogger(getClass().getName());


    public void fileTypeHandler(List<File> files)throws Exception{
        action = false;
        try{

            for (int i = 0; i < files.size(); i++) {
                FileReader filereader = new FileReader(files.get(i));
                String firstLn;
                firstLn = read_firstL(filereader);
                System.out.println(firstLn);
                temp = files.get(i);
                if (firstLn.equals("dateidclickcost") && clickPath != null) {
                    Alert a1 = new Alert(Alert.AlertType.WARNING , "Another valid clicklog file was selected! \n Replace " + clickPath.getName() + " with " + temp.getName() + " ?", ButtonType.OK, ButtonType.CANCEL);
                    replace_click(a1);
                    action = true;
                }else if(firstLn.equals("dateidclickcost")){
                    setClickPath(files.get(i).getPath());
                    notFound.remove("clicklog");
                    action =true;
                }
                if (firstLn.equals("dateidgenderageincomecontextimpressioncost")&&impressionPath != null) {
                    Alert a1 = new Alert(Alert.AlertType.WARNING , "Duplicate impressionlog selected file  found! \n Replace " + impressionPath.getName() + " with " + temp.getName() + " ?", ButtonType.OK, ButtonType.CANCEL);
                    replace_imp(a1);
                    action =true;
                }else if(firstLn.equals("dateidgenderageincomecontextimpressioncost")){
                    setImpressionPath(files.get(i).getPath());
                    notFound.remove("impressionlog");
                    action =true;
                }
                if (firstLn.equals("entrydateidexitdatepagesviewedconversion")&& serverPath != null) {
                    Alert a1 = new Alert(Alert.AlertType.WARNING , "Duplicate serverlog selected file found! \n Replace " + serverPath.getName() + " with " + temp.getName() + " ?", ButtonType.OK, ButtonType.CANCEL);
                    replace_server(a1);
                    action =true;
                }else if(firstLn.equals("entrydateidexitdatepagesviewedconversion")){
                    setServerPath(files.get(i).getPath());
                    notFound.remove("serverlog");
                    action =true;
                }
            }
        }catch (Exception ignored){}
        if(!action){
            Alert alert = new Alert(Alert.AlertType.WARNING ,"No valid csvfiles selected" , ButtonType.OK);
            alert.showAndWait();
        }
        else if(!notFound.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING ,"files: " + notFound + " were not found" , ButtonType.OK);
            alert.showAndWait();
        }
    }
    public void directoy_handler(File dir)throws Exception{
        if(dir.list() == null){
            throw new FileNotFoundException("directory is empty");
        }
        File[] files = dir.listFiles();
        try {
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
                    if(firstLn.equals("dateidclickcost") & clickPath != null && notFound.contains("clicklog")){
                        Alert a1 = new Alert(Alert.AlertType.WARNING , "Duplicate clicklog file in directory " + dir.getName() + " found! \n replace " + clickPath.getName() + " with " + temp.getName() + " ?", ButtonType.OK, ButtonType.CANCEL);
                        replace_click(a1);
                        action = true;
                    }  else if (firstLn.equals("dateidclickcost") && notFound.contains("clicklog")) {
                        setClickPath(files[i].getAbsolutePath());
                        action = true;
                        logger.log(Level.INFO, "filepath " + files[i].getAbsolutePath() + "saved");
                    }else if (firstLn.equals("dateidclickcost")) {
                        Alert a1 = new Alert(Alert.AlertType.WARNING , "Another clicklog was found in " + dir.getName() + " \n do you want to replace " + clickPath.getName() + " with " + temp.getName() + " ?", ButtonType.OK, ButtonType.CANCEL);
                        replace_click(a1);
                        action = true;
                    }
                    if(firstLn.equals("dateidgenderageincomecontextimpressioncost") & impressionPath != null && notFound.contains(2)){
                        Alert a1 = new Alert(Alert.AlertType.WARNING , "Duplicate impressionlog file in directory: " + dir.getName() + " found! \n replace " + clickPath.getName() + " with " + temp.getName() + " ?", ButtonType.OK, ButtonType.CANCEL);
                        replace_imp(a1);
                        action = true;
                    }   else if (firstLn.equals("dateidgenderageincomecontextimpressioncost") && notFound.contains("impressionlog")) {
                        setImpressionPath(files[i].getAbsolutePath());
                        action = true;
                        logger.log(Level.INFO, "filepath " + files[i].getAbsolutePath() + "saved");
                    } else if (firstLn.equals("dateidgenderageincomecontextimpressioncost")) {
                        Alert a1 = new Alert(Alert.AlertType.WARNING , "Another impressionlog was found in " + dir.getName() + " \n do you want to replace " + clickPath.getName() + " with: " + temp.getName() + " ?", ButtonType.OK, ButtonType.CANCEL);
                        action = true;
                        replace_imp(a1);
                    }
                    if(firstLn.equals("entrydateidexitdatepagesviewedconversion") & serverPath != null && notFound.contains("serverlog")){
                        Alert a1 = new Alert(Alert.AlertType.WARNING , "Duplicate serverlog file in directory: " + dir.getName() + " found! \n replace " + clickPath.getName() + " with " + temp.getName() + " ?", ButtonType.OK, ButtonType.CANCEL);
                        replace_server(a1);
                        action = true;
                    }   else if (firstLn.equals("entrydateidexitdatepagesviewedconversion") && notFound.contains("serverlog")) {
                        setServerPath(files[i].getAbsolutePath());
                        logger.log(Level.INFO, "filepath " + files[i].getAbsolutePath() + "saved");
                        action = true;
                    } else if (firstLn.equals("entrydateidexitdatepagesviewedconversion")) {
                        Alert a1 = new Alert(Alert.AlertType.WARNING , "Another serverlog was found in " + dir.getName() + " \n do you want to replace " + clickPath.getName() + " with: " + temp.getName() + " ?", ButtonType.OK, ButtonType.CANCEL);
                        replace_server(a1);
                        action = true;
                    }
                }
            }
        }catch (Exception e){throw new RuntimeException("error with the file system");}
        if(notFound.contains("clicklog")&& clickPath != null){
            notFound.remove("clicklog");
        }
        if(notFound.contains("impressionlog")&&impressionPath != null){
            notFound.remove("impressionlog");
        }
        if(notFound.contains("serverlog")&&serverPath != null){
            notFound.remove("serverlog");
        }
        if(!action){
            Alert alert = new Alert(Alert.AlertType.WARNING ,"No valid csvfiles selected" , ButtonType.OK);
            alert.showAndWait();
        }
        else if(!notFound.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING ,"files: " + notFound + "were not found" , ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void replace_click(Alert a1) {
        a1.setTitle("duplicate clicklog");
        ((Button) a1.getDialogPane().lookupButton(ButtonType.OK)).setText("replace");
        ((Button) a1.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("keep");
        Optional<ButtonType> result = a1.showAndWait();
        if(result.isPresent()) {
            if (result.get() == ButtonType.OK) {
                logger.log(Level.INFO, "replacing clickpath");
                try{setClickPath(temp.getAbsolutePath());
                notFound.remove("clicklog");
                }
                catch(Exception ignored){}
            }
        }
    }

    private void replace_server(Alert a1) {
        a1.setTitle("duplicate serverlog");
        ((Button) a1.getDialogPane().lookupButton(ButtonType.OK)).setText("replace");
        ((Button) a1.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("keep");
        Optional<ButtonType> result = a1.showAndWait();
        if(result.isPresent()) {
            if (result.get() == ButtonType.OK) {
                logger.log(Level.INFO, "replacing serverlog");
                try{setServerPath(temp.getAbsolutePath());
                notFound.remove("serverlog");}
                catch (Exception ignored){}
            }
        }
    }

    private void replace_imp(Alert a1) {
        a1.setTitle("duplicate impressionlog");
        ((Button) a1.getDialogPane().lookupButton(ButtonType.OK)).setText("replace");
        ((Button) a1.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("keep");
        Optional<ButtonType> result = a1.showAndWait();
        if (result.isPresent()) {
            if (result.get() == ButtonType.OK) {
                logger.log(Level.INFO, "replacing impressionlog");
                try{
                setImpressionPath(temp.getAbsolutePath());
                notFound.remove("impressionlog");
                }catch (Exception ignored){}}
        }
    }


    public void setClickPath(String csvPath) throws Exception{
        logger.log(Level.INFO, "Setting click_log path.");
        try{
            File clickfile = new File(csvPath);
            FileReader filereader = new FileReader(clickfile);
            String firstln;
            firstln = read_firstL(filereader);
            if(firstln.equals("dateidclickcost")){
                notFound.remove("clicklog");
                this.clickPath = clickfile;
            }else{
                logger.log(Level.SEVERE,"incompatible clicklog csv file");
                Alert a = new Alert(Alert.AlertType.WARNING, "invalid clicklog selected!");
                a.show();
                throw new RuntimeException("incompatible clicklog csv selected!");
            }
        }
        catch (FileNotFoundException e){ logger.log(Level.WARNING,"no clicklog file inputted.");
            Alert a = new Alert(Alert.AlertType.WARNING, "Error no clicklog selected!  " + e);
            a.show();
            throw e;
        }catch (IOException e){
            logger.log(Level.SEVERE,"error with reading the clicklog");
            Alert a = new Alert(Alert.AlertType.WARNING, "Error reading clicklog: " + e);
            a.show();
            throw e;
        }catch (RuntimeException e){throw e;}
        catch (Exception ignored){}
    }

    public boolean all_loaded(){
        return notFound.isEmpty();
    }

    public void setImpressionPath(String impressionPath) throws Exception{
        logger.log(Level.INFO, "Setting impression_log path.");
        try{
            File impressionFile = new File(impressionPath);
            FileReader filereader = new FileReader(impressionFile);
            String firstln;
            firstln = read_firstL(filereader);
            if(firstln.equals("dateidgenderageincomecontextimpressioncost")){
                notFound.remove("impressionlog");
                this.impressionPath = impressionFile;
            }else{
                logger.log(Level.SEVERE,"incompatible impressionlog csv file");
                Alert a = new Alert(Alert.AlertType.WARNING, "invalid impressionlog selected!");
                a.show();
                throw new RuntimeException("incompatible impressionlog selected!");
            }
        }
        catch (FileNotFoundException e){ logger.log(Level.WARNING,"no impressionlog file inputted.");
            Alert a = new Alert(Alert.AlertType.WARNING, "Error no Impression Log selected!  " + e);
            a.show();
        }catch (IOException e) {
            logger.log(Level.SEVERE, "error with reading the impressionlog");
            Alert a = new Alert(Alert.AlertType.WARNING, "Error reading Impression Log: " + e);
            a.show();
        }catch (RuntimeException e){throw e;
        }catch (Exception ignored){}}

    private String read_firstL(FileReader filereader) throws IOException, CsvValidationException {
        CSVReader csvReader = new CSVReader(filereader);
        System.out.println("reading");
        String[] nextRecord;

        nextRecord = csvReader.readNext();
        String firstLn = "";
        for (int j = 0; j < nextRecord.length; j++) {
            firstLn += nextRecord[j];
        }
        firstLn = firstLn.toLowerCase();
        return firstLn.replaceAll("\\s+", "");
    }

    public void setServerPath(String serverPath)  throws Exception{
        logger.log(Level.INFO, "Setting server_log path.");
        try{
            File serverFile = new File(serverPath);
            FileReader filereader = new FileReader(serverFile);
            String firstln;
            firstln = read_firstL(filereader);
            if(firstln.equals("entrydateidexitdatepagesviewedconversion")){
                notFound.remove("serverlog");
                this.serverPath = serverFile;
            }else{
                logger.log(Level.SEVERE,"incompatible serverlog csv file");
                Alert a = new Alert(Alert.AlertType.WARNING, "invalid serverlog selected!");
                a.show();
                throw new RuntimeException();
            }
        }
        catch (FileNotFoundException e){ logger.log(Level.WARNING,"no serverlog file inputted.");
            Alert a = new Alert(Alert.AlertType.WARNING, "Error no serverlog selected!  " + e);
            a.show();
        }catch (IOException e){
            logger.log(Level.SEVERE,"error with reading the serverlog");
            Alert a = new Alert(Alert.AlertType.WARNING, "Error reading serverlog Log: " + e);
            a.show();
        }catch (RuntimeException e){throw e;
        }catch (Exception ignored){}}


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

