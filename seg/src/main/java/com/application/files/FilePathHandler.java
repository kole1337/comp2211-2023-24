package com.application.files;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FilePathHandler {
    private File clickPath;
    private File impressionPath;
    private File serverPath;

    public File getFilesPath() {
        return filesPath;
    }

    public void setFilesPath(File filesPath) {
        this.filesPath = filesPath;
    }

    private File filesPath;
    private Logger logger = Logger.getLogger(getClass().getName());


    public void fileTypeHandler(List<File> files){
        try{
            for (int i = 0; i < files.size(); i++) {


                FileReader filereader = new FileReader(files.get(i));
                CSVReader csvReader = new CSVReader(filereader);
                System.out.println("readng");
                String[] nextRecord;

                nextRecord = csvReader.readNext();
                String firstLn = "";
                for (int j = 0; j < nextRecord.length; j++) {
                    firstLn += nextRecord[j];
                }
                firstLn = firstLn.toLowerCase();
                firstLn = firstLn.replaceAll("\\s+", "");
                System.out.println(firstLn);

                if (firstLn.equals("dateidclickcost")) {
                    setClickPath(files.get(i).getAbsolutePath());
                }
                if (firstLn.equals("dateidgenderageincomecontextimpressioncost")) {
                    setImpressionPath(files.get(i).getAbsolutePath());
                }
                if (firstLn.equals("entrydateidexitdatepagesviewedconversion")) {
                    setServerPath(files.get(i).getAbsolutePath());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
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

