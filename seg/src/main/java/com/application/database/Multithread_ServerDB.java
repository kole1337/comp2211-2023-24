package com.application.database;

import com.application.dashboard.DashboardController;
import com.application.files.FilePathHandler;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Multithread_ServerDB implements Runnable  {
    private Thread mainthread;
    private DataManager dat;
    private DashboardController dash;
    private Logger logger = Logger.getLogger(DashboardController.class.getName());

    private FilePathHandler fph;

    public Multithread_ServerDB(FilePathHandler fph,DashboardController dash,Thread main){
        this.fph = fph;
        this.dash = dash;
        this.dat = new DataManager();
        this.mainthread = main;

    }

    void writeServerDB() throws Exception {

        FileReader clickReader = new FileReader(fph.getServerPath());
        CSVReader clickCSVReader = new CSVReader(clickReader);
        System.out.println("reading server_log CSV");
        String[] nextRecord;

        nextRecord = clickCSVReader.readNext();


        while ((nextRecord = clickCSVReader.readNext()) != null) {
            try {
                if ("n/a".equals(nextRecord[2])) {
                    dat.addServerLog(nextRecord[0], nextRecord[1], null,
                            Integer.parseInt(nextRecord[3]), nextRecord[4]);
                } else {
                    dat.addServerLog(nextRecord[0], nextRecord[1], nextRecord[2],
                            Integer.parseInt(nextRecord[3]), nextRecord[4]);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            catch (Exception e){
                throw new RuntimeException("failed to insert into table");
            }
        }
        logger.log(Level.INFO,"Loaded server_log.csv");
        logger.log(Level.INFO,"Notifying main thread");
        mainthread.notify();
        wait();
        dash.serverLoadedLabel.setText("server_log.csv: loaded");
        dash.setserverLoaded(true);
    }

    @Override
    public void run() {
        try{this.writeServerDB();}
        catch (Exception e){
            logger.log(Level.SEVERE,"SQL error");
            e.printStackTrace();
        }
    }
}
