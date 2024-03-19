package com.application.database;

import com.application.dashboard.DashboardController;
import com.application.files.FilePathHandler;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Multithread_ClicksDb implements Runnable{


    private Thread mainthread;
    private DataManager dat;
    private DashboardController dash;
    private Logger logger = Logger.getLogger(DashboardController.class.getName());

    private FilePathHandler fph;

    public Multithread_ClicksDb(FilePathHandler fph,DashboardController dash,Thread main){
        this.fph = fph;
        this.dash = dash;
        //this.dat = new DataManager();
        //this.mainthread = main;

    }
    private void writeClicksDB() throws Exception {
        try (FileReader clickReader = new FileReader(fph.getClickPath());
             CSVReader clickCSVReader = new CSVReader(clickReader)) {
            System.out.println("Reading click_log CSV");
            String[] nextRecord;

            nextRecord = clickCSVReader.readNext(); //read the header

            while ((nextRecord = clickCSVReader.readNext()) != null) {
                try {
                    dat.addClickLog(nextRecord[0], nextRecord[1], Double.parseDouble(nextRecord[2]));
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "SQL error occurred", e);
                    // Handle SQLException appropriately
                }
            }

            logger.log(Level.INFO, "Loaded clicks_log.csv");
            synchronized (dash) {
                logger.log(Level.INFO, "Notifying main thread");
                dash.notify(); // Notify main thread after completion
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error occurred during insertion", e);
            throw new RuntimeException("Failed to insert into table");
        }
    }

    @Override
    public void run() {
        try{this.writeClicksDB();}
        catch (Exception e){
            logger.log(Level.SEVERE,"SQL error");
            e.printStackTrace();
        }
    }
}
