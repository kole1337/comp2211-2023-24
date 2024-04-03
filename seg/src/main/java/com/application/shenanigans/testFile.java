package com.application.shenanigans;

import com.application.database.DataManager;
import com.application.database.DbConnection;
import com.application.database.Multithread_ImpressionDb;
import com.application.files.FileChooserWindow;
import com.application.files.FilePathHandler;
import com.application.logger.LogAction;
import com.application.styles.checkStyle;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.time.Instant;

import static javafx.application.Application.launch;

/**
 * This is a test file. It should be ignored.
 * */
public class testFile extends Application {


    @Override
    public void start(Stage stage) throws Exception {


        checkStyle obj1 = new checkStyle();

        obj1.writeTheme("dark");



    }


    public static void main(String[] args) {
        launch(args);
    }
}
