package com.application.shenanigans;

import com.application.database.*;
import com.application.files.FileChooserWindow;
import com.application.files.FilePathHandler;
import com.application.logger.LogAction;
import com.application.styles.checkStyle;
import eu.hansolo.tilesfx.Tile;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static javafx.application.Application.launch;

/**
 * This is a test file. It should be ignored.
 * */
public class testFile{
    private static final String DB_URL = "jdbc:mysql://localhost:3306/adda";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "jojo12345";
    private static final long CHECK_INTERVAL_MS = 3000; // Check every 30 secs
    static DbConnection db = new DbConnection();
    static UserManager um = new UserManager();

    static String currentUser = "test30";
    public static void main(String[] args){


//        um.insertUser("test30","1234","user");

        Timer timer = new Timer();
        timer.schedule(new UserExistenceTask(), 0, CHECK_INTERVAL_MS);

    }

    private static class UserExistenceTask extends TimerTask {
        @Override
        public void run() {
            checkUserExistence();
        }

        private void checkUserExistence() {

            if(um.checkUserExistence(currentUser)){
                System.out.println("Exists!");
            }else{
                System.out.println("Gooner!");
            }
        }
    }

}
