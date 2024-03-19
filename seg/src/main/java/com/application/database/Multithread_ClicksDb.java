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
        this.dat = new DataManager();
        this.mainthread = main;

    }
    private void writeClicksDB() throws Exception {

        FileReader clickReader = new FileReader(fph.getClickPath());
        CSVReader clickCSVReader = new CSVReader(clickReader);
        System.out.println("reading  click_log CSV");
        String[] nextRecord;

        nextRecord = clickCSVReader.readNext();



        while((nextRecord = clickCSVReader.readNext()) != null ) {
            //TODO:figure out the sql exception thrown when 2 sql exceptions are entered at once
          try {
              dat.addClickLog(nextRecord[0], nextRecord[1], Double.parseDouble(nextRecord[2]));
          }catch (SQLException e){
                e.printStackTrace();
          }catch (Exception e){
              throw new RuntimeException("failed to insert into table");
          }
        }

        logger.log(Level.INFO,"Loaded clicks_log.csv");
        logger.log(Level.INFO,"Notifying main thread");
        mainthread.notify();
        wait();
        dash.clicksLoadedLabel.setText("clicks_log.csv: loaded");
        dash.setClicksLoaded(true);
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
