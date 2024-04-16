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
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static javafx.application.Application.launch;

/**
 * This is a test file. It should be ignored.
 * */
public class testFile extends Application{

    public static void main(String[] args) throws Exception {


        DbConnection db = new DbConnection();
        testServerThread obj = new testServerThread();
        testClickThread obj1 = new testClickThread();
        Multithread_ImpressionDb obj2 = new Multithread_ImpressionDb();

        obj.main(new ArrayList<>());
        obj1.main(new ArrayList<>());
        obj2.main(new ArrayList<>());

        db.closeConn();
        
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
//        DbConnection db = new DbConnection();

        TilePane r = new TilePane();
        Scene sc = new Scene(r, 200, 200);

        stage.setScene(sc);

        stage.show();
    }
}
