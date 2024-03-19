package com.application.database;

import com.application.dashboard.DashboardController;
import com.application.files.FilePathHandler;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Multithread_ImpressionsDB implements Runnable {


    private Thread mainthread;
    private DataManager dat;
    private DashboardController dash;
    private Logger logger = Logger.getLogger(DashboardController.class.getName());

    private FilePathHandler fph;

    public Multithread_ImpressionsDB( FilePathHandler fph, DashboardController dash, Thread main) {
        this.fph = fph;
        this.dash = dash;
        this.dat = new DataManager();
        this.mainthread = main;
    }
    private void writeImpressionsDB() throws Exception {

        FileReader impressionReader = new FileReader(fph.getImpressionPath());
        CSVReader clickCSVReader = new CSVReader(impressionReader);
        System.out.println("reading impressions_log CSV");
        String[] nextRecord;

        nextRecord = clickCSVReader.readNext();


            while ((nextRecord = clickCSVReader.readNext()) != null) {
                try {
                dat.addImpressionLog(nextRecord[0], nextRecord[1],
                        nextRecord[2], nextRecord[3], nextRecord[4],
                        nextRecord[5], Double.parseDouble(nextRecord[6]));
                }catch(SQLException e){
                    //TODO: figure out the exception that specifically comes when 2
                    e.printStackTrace();
                }catch (Exception e){
                    throw new RuntimeException("failed to insert into table");
                }
            }

        logger.log(Level.INFO,"Loaded impressions_log.csv");
        logger.log(Level.INFO,"Notifying main thread");
        mainthread.notify();
        wait();
        dash.impressionLoadedLabel.setText("impression_log.csv: loaded");
        dash.setimpressionsLoaded(true);
    }
    @Override
    public void run() {
        try{this.writeImpressionsDB();}
        catch (Exception e){
            logger.log(Level.SEVERE,"SQL error");
            e.printStackTrace();
        }
    }
}